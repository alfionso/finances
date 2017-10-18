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

import 'whatwg-fetch';
import * as api from '../config/api';
import * as i18n from '../config/i18n';
import {showAlert} from '../config/configActions';


// request list
export const FETCH_TRANSACTION = 'FETCH_TRANSACTION';
export const FETCH_TRANSACTION_SUCCESS = 'FETCH_TRANSACTION_SUCCESS';
export const FETCH_TRANSACTION_FAILURE = 'FETCH_TRANSACTION_FAILURE';
export const TRANSACTION_UPDATE = 'TRANSACTION_UPDATE';
export const TRANSACTION_UPDATE_STATUS = 'TRANSACTION_UPDATE_STATUS';
export const TRANSACTION_REMOVE = 'TRANSACTION_REMOVE';
export const TABLE_SAVE_CONFIGURATION = 'TABLE_SAVE_CONFIGURATION';



export function refreshTransaction(transaction){
    return {
        type:TRANSACTION_UPDATE,
        payload: transaction
    }
}

export function refreshRemoveTransaction(id){
    return {
        type:TRANSACTION_REMOVE,
        payload: id
    }
}

export function fetchTransaction() {
    return function(dispatch) {
        return api.get(api.urls.transaction.all, dispatch)
            .then((response) => {
                dispatch({type: FETCH_TRANSACTION_SUCCESS, payload: response})
            })
            .catch(error=>{});
    }
}

export function updateTransaction(transaction){
    
    return function(dispatch){
        if(validateTransaction(transaction, dispatch)){
            let url = api.withParams(api.urls.transaction.update, [transaction.id]);
            api.put(url,transaction,dispatch)
                .then((data) => {
                    dispatch({type: TRANSACTION_UPDATE, payload: data});
                    if(data.scheduleDate){
                        dispatch(showAlert({
                            message: i18n.getTextWithParam(MESSAGE_CODE.CODE_SCHEDULE_MESSAGE_CONFIRMATION, api.formatStringToShort(data.scheduleDate)),
                            severity: api.ERROR_SEVERITY.INFORMATION,
                        }));
                    }
                })
                .catch(error=>{});
        }        
    }
}

export function deleteTransaction(id){
    return function (dispatch) {
        let url = api.withParams(api.urls.transaction.delete, [id]);
        api.del(url, dispatch)
            .then(data => {})
            .catch(error => {});
        
    }
}

export function saveTableConfiguration(field, order){
    return function(dispatch){
        dispatch({
            type: TABLE_SAVE_CONFIGURATION,
            payload: {
                field : field,
                order: order
            }
        });
    }
}

