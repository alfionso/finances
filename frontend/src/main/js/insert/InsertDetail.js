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
import { connect } from 'react-redux';
import * as api from '../config/api';
import {showAlert} from '../config/configActions';
import {fetchInsertRequest,
    updateInsertScheduleDate,
    updateInsertRequest,
    startProcessInsertion,
    pauseProcessInsertion,
    resumeProcessInsertion,
    abortProcessInsertion
} from './InsertActions';
import { 
    Grid,
    Row,
    Col,
    Form,
    FormGroup,
    Well,
    ProgressBar,
    Panel,
    ControlLabel, 
    Button,
    ButtonToolbar,
    Label,
    Glyphicon,
    Alert,
    Modal,
    
} from 'react-bootstrap';
import NotEditableFieldForm from '../components/fields/NotEditableFieldForm';
import DatetimeRangePicker from 'react-bootstrap-datetimerangepicker';
import css from 'bootstrap-daterangepicker/daterangepicker.css';
import moment from 'moment';
import DateTimeEditor from '../components/fields/DateTimeEditor';

@connect((store) => {
    return {
        insert: store.insert,
        configuration: store.configuration
    };
})
export default class InsertDetail extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            showModal : false,
            question : '',
            handleAction : null
        }
    }

    componentDidMount() {
        this.props.dispatch(fetchInsertRequest(this.props.params.id));
    };

    handleChangeDate = (newStringDate) =>{
        const i18n = this.props.configuration.i18n;
        this.props.dispatch(updateInsertScheduleDate(api.formatStringToMoment(newStringDate)));
        this.openModal(i18n.texts['label.insert.detail.start.question'], this.doSave);
    };
    
    doSave = () => {
        this.closeModal();
        const {request, validation} = this.props.insert;
        if (validation.date){
            this.props.dispatch(updateInsertRequest(request));
        }else {
            let info = {
                severity: api.ERROR_SEVERITY.WARNING,
                message: 'All fields are mandatory',
                enabled: true
            };
            this.props.dispatch(showAlert(info));
        }
    };

    handleStart = (i18n) =>{
        this.openModal(i18n.texts['label.insert.detail.start.question'], this.doStart);
    };

    doStart = () =>{
        this.closeModal();
        this.setState({ showModal : false, question: ''});
        const {request} = this.props.insert;
        this.props.dispatch(startProcessInsertion(request.id));
    };
    
    handlePause = (i18n) =>{
        this.openModal(i18n.texts['label.insert.detail.pause.question'], this.doPause);
    };
    
    doPause = () => {
        this.closeModal();
        const {request} = this.props.insert;
        this.props.dispatch(pauseProcessInsertion(request.id));
    };
    
    handleResume = (i18n) => {
        this.openModal(i18n.texts['label.insert.detail.resume.question'], this.doResume);
    };
    
    doResume = () => {
        this.closeModal();
        const {request} = this.props.insert;
        this.props.dispatch(resumeProcessInsertion(request.id));
    };
    
    handleAbort = (i18n) => {
        this.openModal(i18n.texts['label.insert.detail.abort.question'], this.doAbort);
    };
    
    doAbort = () => {
        this.closeModal();
        const {request} = this.props.insert;
        this.props.dispatch(abortProcessInsertion(request.id));
    };
    
    statusFormatter = (request, i18n) => {
        let style = api.getStyleInsertStatus(request.status);
        let errorCode = null;
        if(request.statusCode){
            errorCode = (<Alert bsStyle="danger" style={{marginTop:'10px'}}>{i18n.texts[request.statusCode]}</Alert>);
        }
        return (
            <span>
                <Label bsStyle={style}>{i18n.texts[request.status]}</Label>
                {errorCode}
            </span>
        );
    };
    
    actionsFormatter = (i18n) => {
        const {request} = this.props.insert;
        let pauseButton, resumeButton, abortButton, startButton;
        switch (request.status){
            case api.INSERT_REQUEST_STATUS.IN_PROGRESS:
                pauseButton = (<Button disabled={this.props.insert.sendingPause} bsStyle="warning" onClick={() => this.handlePause(i18n)}><Glyphicon glyph="pause" /> {i18n.texts['label.pause']}</Button>);
                abortButton = (<Button bsStyle="danger" disabled="true" title={i18n.texts['label.insert.detail.abort.warning.title']} onClick={() => this.handleAbort(i18n)}><Glyphicon glyph="ban-circle" /> {i18n.texts['label.abort']}</Button>);
                break;
            case api.INSERT_REQUEST_STATUS.MISSED_SCHEDULE:
            case api.INSERT_REQUEST_STATUS.NOT_STARTED:
                startButton = (<Button bsStyle="primary" onClick={() => this.handleStart(i18n)}><Glyphicon glyph="play" /> {i18n.texts['label.start']}</Button>);
                abortButton = (<Button bsStyle="danger" disabled="true" title={i18n.texts['label.insert.detail.abort.warning.title']} onClick={() => this.handleAbort(i18n)}><Glyphicon glyph="ban-circle" /> {i18n.texts['label.abort']}</Button>);
                break;
            case api.INSERT_REQUEST_STATUS.PAUSED:
                abortButton = (<Button bsStyle="danger" onClick={() => this.handleAbort(i18n)}><Glyphicon glyph="ban-circle" /> {i18n.texts['label.abort']}</Button>);
                resumeButton =(<Button bsStyle="info"  onClick={() => this.handleResume(i18n)}><Glyphicon glyph="repeat" /> {i18n.texts['label.resume']}</Button>);
                break;
        }
        
        return (
            <ButtonToolbar>
                {startButton}
                {pauseButton}
                {resumeButton}
                {abortButton}
            </ButtonToolbar>
        )
    };
    
    scheduleFormatter = (request) => {
        if(request.status === api.INSERT_REQUEST_STATUS.NOT_STARTED ||
            request.status === api.INSERT_REQUEST_STATUS.PAUSED){
            return (
                <DateTimeEditor onUpdate={this.handleChangeDate}
                                defaultValue={request.scheduleDate}/>
            );
        }else{
            return <NotEditableFieldForm>{api.formatStringToShort(request.scheduleDate)}</NotEditableFieldForm>;
        }
    };

    closeModal = () => {
        this.setState({ showModal : false, question: ''});
    };

    openModal = (question, handleAction) => {
        this.setState({showModal:true, question : question, handleAction : handleAction});
    };
    createModal = (i18n) =>{
        return (
            <Modal show={this.state.showModal} onHide={this.closeModal}>
                <Modal.Body>
                    <h4>{this.state.question}</h4>
                </Modal.Body>
                <Modal.Footer>
                    <Button bsStyle='link' onClick={this.closeModal}>{i18n.texts['label.close']}</Button>
                    <Button bsStyle='primary' onClick={this.state.handleAction}>{i18n.texts['label.accept']}</Button>
                </Modal.Footer>
            </Modal>
        )
    };


    render() {
        const {request, validation} = this.props.insert;
        const i18n = this.props.configuration.i18n;
        let metadataName = '';
        let urlMetadata =  api.urls.metadata+request.id+'/'+request.fileName;
               
        let urlSuccessLog = '';
        let nameSuccessLog = '';
        if(request.successLogPath){
            urlSuccessLog = api.urls.reports + request.successLogPath;
            nameSuccessLog = request.successLogPath;
        }

        let urlErrorLog = '';
        let nameErrorLog = '';
        if(request.errorLogPath){
            urlErrorLog = api.urls.reports + request.errorLogPath;
            nameErrorLog = request.errorLogPath;
        }
        
        
        return (
            <div className="container">
                <h3>{i18n.texts['label.insert.detail.title']}</h3>
                <Grid>
                    <Row>
                        <Col md={7} mdOffset={1}>
                            <Form horizontal>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.detail.id']}
                                    </Col>
                                    <Col md={2}>
                                        <NotEditableFieldForm>{request.id}</NotEditableFieldForm>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.detail.metadata.link']}
                                    </Col>
                                    <Col md={6}>
                                        <a href={urlMetadata}>
                                            <NotEditableFieldForm>{request.fileName}</NotEditableFieldForm>
                                        </a>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.detail.log.success.link']}
                                    </Col>
                                    <Col md={6}>
                                        <a href={urlSuccessLog}><NotEditableFieldForm>{nameSuccessLog}</NotEditableFieldForm></a>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.detail.log.error.link']}
                                    </Col>
                                    <Col md={6}>
                                        <a href={urlErrorLog}><NotEditableFieldForm>{nameErrorLog}</NotEditableFieldForm></a>
                                    </Col>
                                </FormGroup>
                                <FormGroup validationState={validation.date?null:'error'}>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.schedule.date']}
                                    </Col>
                                    <Col md={4}>
                                        {this.scheduleFormatter(request)}
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.start.date']}
                                    </Col>
                                    <Col md={4}>
                                        <NotEditableFieldForm>{api.formatStringToShort(request.startDate)}</NotEditableFieldForm>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.insert.end.date']}
                                    </Col>
                                    <Col md={4}>
                                        <NotEditableFieldForm>{api.formatStringToShort(request.endDate)}</NotEditableFieldForm>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col componentClass={ControlLabel} md={4}>
                                        {i18n.texts['label.status']}
                                    </Col>
                                    <Col md={8}>
                                        <NotEditableFieldForm noline>{this.statusFormatter(request, i18n)}</NotEditableFieldForm>
                                    </Col>
                                </FormGroup>
                                <FormGroup>
                                    <Col md={10} smOffset={3}>
                                        {this.actionsFormatter(i18n)}
                                    </Col>
                                </FormGroup>
                            </Form>
                        </Col>
                        <Col md={3}>
                            <Well><h4>{i18n.texts['label.summary']}</h4>
                                <ul>
                                    <li>
                                        <b style={{color:'#d9534f'}}>{i18n.texts['label.ERROR']}:</b> {request.errorLines}
                                    </li>
                                    <li>
                                        <b style={{color:'#5cb85c'}}>{i18n.texts['label.success']}:</b> {request.successLines}
                                    </li>
                                    <li>
                                        <b>{i18n.texts['label.total']}: </b> {request.totalLines}
                                    </li>
                                </ul>
                            </Well>
                        </Col>
                    </Row>
                    <Row className="finances-row-bottom">
                        <FormGroup>
                            <Col md={10} mdOffset={1}>
                                <Panel header={i18n.texts['label.insert.detail.process.title']} bsStyle="info">
                                    <ProgressBar>
                                        <ProgressBar bsStyle="success" now={(request.successLines/request.totalLines)*100} key={1} />
                                        <ProgressBar striped bsStyle="danger" now={(request.errorLines/request.totalLines)*100} key={3} />
                                    </ProgressBar>
                                </Panel>
                            </Col>
                        </FormGroup>
                        
                    </Row>
                    
                </Grid>


                {this.createModal(i18n)}
            </div>
        );
    }
}
