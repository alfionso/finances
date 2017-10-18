/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import {Form, FormGroup, ControlLabel, FormControl, HelpBlock, Button,Grid, Row, Col, InputGroup,Glyphicon, Alert} from 'react-bootstrap';
import {connect} from 'react-redux';
import {addInsertRequest, updateInsertDate, cleanInsertform, updateInsertFile} from '../insert/InsertActions';
import {showAlert} from '../config/configActions';
import * as api from '../config/api';
import DateTimeEditor from '../components/fields/DateTimeEditor';
import {LinkContainer} from 'react-router-bootstrap';
import moment from 'moment';
import Dropzone from 'react-dropzone';


@connect((store) => {
    return {
        insert: store.insert,
        configuration: store.configuration
    };
})
export default class InsertForm extends React.Component {
    
    constructor() {
        super();
    }
    componentWillMount() {}
    componentDidMount() {
        this.props.dispatch(cleanInsertform());
    }
    
    handleSubmitClick = (i18n) => {
        const {request, validation} = this.props.insert;
        let date = api.formatStringToMoment(request.scheduleDate);

        if(!request.scheduleDate || (date.isValid() && date.isAfter(moment()))){
            if(validation.file){
                this.props.dispatch(addInsertRequest(request));
            }else {
                let info = {
                    severity: api.ERROR_SEVERITY.WARNING,
                    message: i18n.texts['label.insert.form.validation.mandatory.fields'],
                    enabled: true
                };
                this.props.dispatch(showAlert(info));
            }
        }else{
            let info = {
                severity: api.ERROR_SEVERITY.WARNING,
                message: i18n.texts['label.insert.form.validation.date.incorrect'],
                enabled: true
            };
            this.props.dispatch(showAlert(info));
        }
            
    };
    
    handleChangeDate = (newStringDate) =>{
        this.props.dispatch(updateInsertDate(newStringDate));
    };
    
    validationColorHandler = (value) =>{
        if(value != null){
            if(value==true){
                return 'success';
            }else{
                return 'error';
            }
        }else{
            return null;
        }
    };

    handlerOnDrop = (acceptedFiles, rejectedFiles) => {
        if(acceptedFiles.length>0){
            this.props.dispatch(updateInsertFile(acceptedFiles[0]));            
        }

    };
    
    render() {
        const { validation } = this.props.insert;
        const request = this.props.insert.request;
        const {i18n} = this.props.configuration;
        return (
            <div>
                <h3>{i18n.texts['label.insert.form.title']}</h3>
                <Grid>
                    <Row className="finances-row-bottom">
                        <Col md={8} mdOffset={2}>
                            <Form>

                                <FormGroup validationState={this.validationColorHandler(validation.file)}>
                                    <ControlLabel>{i18n.texts['label.insert.form.field.file.name']}</ControlLabel>
                                    <Dropzone 
                                        onDrop={this.handlerOnDrop}
                                        accept="text/csv"
                                        style={{}}
                                        multiple={false}>
                                        <InputGroup>
                                            <FormControl
                                                type="text"
                                                value={request.fileName?request.fileName:''}
                                                placeholder={i18n.texts['label.insert.form.field.file.placeholder']}
                                                editable={false}
                                            />
                                            <InputGroup.Addon>
                                                <Glyphicon glyph="folder-open"/>
                                            </InputGroup.Addon>
                                        </InputGroup>
                                    </Dropzone>
                                    <FormControl.Feedback/>
                                    <HelpBlock>{i18n.texts['label.insert.form.field.file.description']}</HelpBlock>
                                </FormGroup>
                                
                                <FormGroup validationState={this.validationColorHandler(validation.date)}>
                                    <ControlLabel>{i18n.texts['label.insert.form.field.schedule.name']}</ControlLabel>
                                    <Row>
                                        <Col md={4}>
                                            <DateTimeEditor onUpdate={this.handleChangeDate}
                                                            defaultValue={request.scheduleDate}/>
                                        </Col>
                                    </Row>
                                    <HelpBlock>{i18n.texts['label.insert.form.field.schedule.description']}</HelpBlock>
                                </FormGroup>
                                <LinkContainer to="/transactionList">
                                    <Button bsStyle="link" href="/">{i18n.texts['label.cancel']}</Button>
                                </LinkContainer>
                                <Button bsStyle="primary" onClick={() => this.handleSubmitClick(i18n)}>{i18n.texts['label.submit']}</Button>
                            </Form>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <Alert bsStyle="info">
                                <h5>Files examples</h5>
                                <p>Download this files to see and example of the processed file </p>
                                <ul>
                                    <li>
                                        <a href="/samples/data-sample-small.csv">Small file</a>
                                    </li>
                                    <li>
                                        <a href="/samples/data-sample-medium.csv">Medium file</a>
                                    </li>
                                    <li>
                                        <a href="/samples/data-sample-big.csv">Big file</a>
                                    </li>
                                </ul>
                            </Alert>
                        </Col>
                    </Row>
                </Grid>
                
                
            </div>
        );
    }
}
