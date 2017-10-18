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
import { refreshTransaction, refreshRemoveTransaction } from '../transactions/TransactionListActions';
import { refreshInsertRequest } from '../insert/InsertActions';
import * as api from './api';
import { connect } from 'react-redux';

@connect()
export default class WSCommunication extends React.Component {
    
    onMessage = (evet) =>{
        var message;
        try{
            if(evet && evet.data){
                message = JSON.parse(evet.data);    
            }
        }catch (e){
            console.log('WS ERROR: '+e);
        }
        if(message && message.type && message.entity){
            switch (message.type){
                case api.WS_TYPE.TRANSACTION_UPDATE:{
                    this.props.dispatch(refreshTransaction(message.entity));
                    break;
                }
                case api.WS_TYPE.TRANSACTION_REMOVE:{
                    this.props.dispatch(refreshRemoveTransaction(message.id));
                    break;
                }
                case api.WS_TYPE.INSERT_UPDATE:{
                    this.props.dispatch(refreshInsertRequest(message.entity));
                    break;
                }
            }
        }
        
    };

    onError = (evet) =>{
        console.log(evet);
    };
    
    componentDidMount(){
        this.connection = new WebSocket(api.urls.ws);
        this.connection.onmessage = this.onMessage;
        this.connection.onerror = this.onError;
    }

    componentWillUnmount() {
        this.connection.close()
    }

    render() {
        return (
            null
        );
    }
}