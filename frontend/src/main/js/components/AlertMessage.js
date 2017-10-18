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
import { Alert } from 'react-bootstrap';
import { connect } from 'react-redux';
import {removeAlert} from '../config/configActions';
import * as api from '../config/api';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class AlertMessage extends React.Component {
    constructor(props){
        super(props);
        this.timer = null;
    }
    

    componentWillUnmount() {
        clearTimeout(this.timer);
    }
    handleAlertDismiss = () => {
        clearTimeout(this.timer);
        this.props.dispatch(removeAlert());
    };
    
    render() {
        const { alert } = this.props.configuration;
        let style = 'info';
        let title = 'Information';
        let detail = '';
        if(alert.severity === api.ERROR_SEVERITY.WARNING){
            style = 'warning';
            title = 'Warning!';
        }else if(alert.severity === api.ERROR_SEVERITY.ERROR) {
            style = 'danger';
            title = 'Error!';
        }
        if(alert.details && alert.details.exception)
            detail = alert.details.exception;
        
        
        
        if(alert.enabled){
            this.timer = setTimeout(() => {
                this.handleAlertDismiss();
            }, 10000);
            return (
                <Alert bsStyle={style} className='alert-finances' onDismiss={this.handleAlertDismiss}>
                    <h4>{title}</h4>
                    <p>{alert.message}</p>
                    <p>{detail}</p>
                </Alert>
            ); 
        }else{
            return null;
        }
        
        
    }
}