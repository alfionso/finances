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
import ReactDOM from 'react-dom';
import {hashHistory} from 'react-router';
import { Grid, Row, Col ,Form, FormGroup, ControlLabel, FormControl, HelpBlock,Alert,
    Button} from 'react-bootstrap';
import { connect } from 'react-redux';
import * as api from '../config/api';
import {login} from '../config/configActions';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class Menu extends React.Component {
    constructor(props){
        super(props);
        this.state = {username:'',password:'', error: ''};
        
    }
    usernameHandler = (e) =>{
      this.setState({username: e.target.value});  
    };
    passwordHandler = (e) =>{
        this.setState({password:e.target.value});
    };
    handleSubmitClick = () =>{        
        fetch(api.urls.config.csrf,{
            method: 'GET',
            credentials: 'include',
            headers: {
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(() => this.submitForm())
        .catch(responseJson => this.setState({error:responseJson}));
        
    };
    
    submitForm = () =>{
        const data = {
            username: this.state.username,
            password: this.state.password
        };

        fetch(api.urls.config.login, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'X-CSRF-TOKEN': api.getToken(),
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: 'username=' + this.state.username + '&password=' + this.state.password
            })
            .then(response => {
                if(response.ok){
                    this.props.dispatch(login(data.username));
                    let userData = api.readUserData();
                    userData.username=data.username;
                    localStorage.setItem('tokenData', JSON.stringify(userData));
                    const targetHref = sessionStorage.Target;
                    sessionStorage.removeItem('Target');
                    hashHistory.push(targetHref || '/');
                }else{
                    console.log(response);
                    response.json()
                        .then(err => this.setState({error: err.details.exception}));
                }
            })
            .then(responseJson => {
                console.log(responseJson)
            })
            .catch(err => this.setState({error: err}));
    };

    keyPressHandle = event =>{
        if (event.charCode === 13) {
            event.preventDefault();
            event.stopPropagation();
            this.submitForm();
        }
    };
    
    componentDidMount(){
        ReactDOM.findDOMNode(this.inputUsername).focus();
    }
    
    render() {
        const i18n = this.props.configuration.i18n;
        return (
            <div class="container">
                
                <Grid>
                    <Row>
                        <Col md={4} mdOffset={4}>
                            <h3>{i18n.texts['label.signin.heading']}</h3>
                            {this.state.error?<Alert bsStyle="warning">{this.state.error}</Alert>:null}
                            <Form>
                                <FormGroup>
                                    <ControlLabel>{i18n.texts['label.signin.username']}</ControlLabel>
                                    <FormControl
                                        inputRef={ref => { this.inputUsername = ref; }}
                                        type="text"
                                        value={this.state.username}
                                        onChange={this.usernameHandler}
                                        onKeyPress={this.keyPressHandle}
                                    />
                                </FormGroup>
                                <FormGroup>
                                    <ControlLabel>{i18n.texts['label.signin.password']}</ControlLabel>
                                    <FormControl
                                        type="password"
                                        value={this.state.password}
                                        onChange={this.passwordHandler}
                                        onKeyPress={this.keyPressHandle}
                                    />
                                </FormGroup>
                                <Button bsStyle="primary"
                                        onKeyPress={this.keyPressHandle}
                                        onClick={this.handleSubmitClick}>{i18n.texts['label.signin.botton.submit']}</Button>
                            </Form>
                        </Col>
                    </Row>
                </Grid>                
            </div>
            
        )
    }
}