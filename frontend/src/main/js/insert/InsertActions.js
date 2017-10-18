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
import moment from 'moment';
import {showAlert} from '../config/configActions';
import { hashHistory } from 'react-router';


// request list
export const FETCH_INSERTS = 'FETCH_INSERTS';
export const FETCH_INSERTS_SUCCESS = 'FETCH_INSERTS_SUCCESS';
export const FETCH_INSERTS_FAILURE = 'FETCH_INSERTS_FAILURE';
export const UPDATE_INSERT_LIST = 'UPDATE_INSERT_LIST';
export const INSERT_TABLE_SAVE_CONFIGURATION = 'INSERT_TABLE_SAVE_CONFIGURATION';

// request insert
export const FETCH_INSERT = 'FETCH_INSERT';
export const FETCH_INSERT_SUCCESS = 'FETCH_INSERT_SUCCESS';
export const FETCH_INSERT_FAILURE = 'FETCH_INSERT_FAILURE';
export const UPDATE_INSERT_DATE = 'UPDATE_INSERT_DATE';
export const UPDATE_INSERT_DATE_FAILURE = 'UPDATE_INSERT_DATE_FAILURE';
export const UPDATE_INSERT = 'UPDATE_INSERT';
export const UPDATE_INSERT_FAILURE = 'UPDATE_INSERT_FAILURE';
export const UPDATE_TO_PAUSE = 'UPDATE_TO_PAUSE';
export const UPDATE_INSERT_PATH = 'UPDATE_INSERT_PATH';
export const UPDATE_INSERT_PATH_FAILURE = 'UPDATE_INSERT_PATH_FAILURE';
export const REQUEST_CLEAN_FORM = 'REQUEST_CLEAN_FORM';
export const TABLE_SAVE_CONFIGURATION = 'TABLE_SAVE_CONFIGURATION';
export const UPDATE_INSERT_FILE = 'UPDATE_INSERT_FILE';
export const UPDATE_INSERT_FILE_FAILURE = 'UPDATE_INSERT_FILE_FAILURE';

export function fetchInsertsRequests() {
    return function(dispatch) {
        return api.get(api.urls.insert.all,dispatch)
            .then((json) => {
                dispatch({type: FETCH_INSERTS_SUCCESS, payload: json});    
            })
            .catch((err) => {
                dispatch({type: FETCH_INSERTS_FAILURE, payload: err})
            })
    }
}

export function fetchInsertRequest(idRequest){
    return function(dispatch) {
        let url = api.withParams(api.urls.insert.get,[idRequest]);
        return api.get(url,dispatch)
            .then((json) => {
                if(json){
                    dispatch({type: FETCH_INSERT_SUCCESS, payload: json})
                }else{
                    let info = {
                        severity: api.ERROR_SEVERITY.WARNING,
                        message: 'Insert request doesn\'t exist',
                        enabled: true
                    };
                    dispatch(showAlert(info));
                }
            })
            .catch((err) => {
                dispatch({type:FETCH_INSERT_FAILURE, payload: err})
            });
    }
}

export function updateInsertScheduleDate(newDate) {
    if(newDate.isValid() && newDate.isAfter(moment())){
        return {
            type: UPDATE_INSERT_DATE,
            payload: api.formatDateToServer(newDate)
        }
    }
    return {
        type: UPDATE_INSERT_DATE_FAILURE
    }
}

export function updateInsertRequest(insertRequest){
    return function (dispatch) {
        if(validateInsertRequest(insertRequest,dispatch)){
            let url = api.withParams(api.urls.insert.update, [insertRequest.id]);
            api.put(url,insertRequest,dispatch)
                .then((data) => {
                    dispatch({type: UPDATE_INSERT, payload: data});
                    let info = {
                        severity: api.ERROR_SEVERITY.INFORMATION,
                        message: 'Insert request save successfully',
                        enabled: true
                    };
                    dispatch(showAlert(info));
                })
                .catch(error => {
                    dispatch({type: UPDATE_INSERT_FAILURE, payload: error});
                });
        }
    }
}

export function startProcessInsertion(idInsert){
    return function (dispatch) {
        let url = api.withParams(api.urls.insert.start, [idInsert]);
        api.post(url, null, dispatch)
            .then(data => {})
            .catch(error=>{});
    }
}
export function pauseProcessInsertion(idInsert){
    return function(dispatch){
        dispatch({
            type:UPDATE_TO_PAUSE,
            payload: true
        });
        let url = api.withParams(api.urls.insert.pause, [idInsert]);
        api.post(url, null, dispatch)
            .then((data) => {})
            .catch(error=>{});
    }
}
export function resumeProcessInsertion(idInsert){
    return function(dispatch){
        let url = api.withParams(api.urls.insert.resume, [idInsert]);
        api.post(url, null, dispatch)
            .then((data) => {})
            .catch(error=>{});
    }
}

export function abortProcessInsertion(idInsert){
    return function(dispatch){
        let url = api.withParams(api.urls.insert.abort, [idInsert]);
        api.post(url, null, dispatch)
            .then((data) => {})
            .catch(error=>{});
    }
}

export function refreshInsertRequest(insertRequest){
    return function (dispatch){
        dispatch(
            {type:UPDATE_INSERT_LIST, payload:insertRequest}
        );
        dispatch(
            {type: UPDATE_INSERT, payload: insertRequest}
        );
    } 
}
export function saveTableConfiguration(field, order){
    return function(dispatch){
        dispatch({
            type: INSERT_TABLE_SAVE_CONFIGURATION,
            payload: {
                field : field,
                order: order
            }
        });
    }
}

// ############## Form actions

export function addInsertRequest(request){
    return function (dispatch) {
        var data = new FormData();
        data.append('insertRequest', new Blob([JSON.stringify(request)], {type: 'application/json'}));
        data.append('content', request.file);

        fetch(api.urls.insert.create, {
            method: 'POST',
            credentials: 'include',
            body: data,
            headers: {
                'X-CSRF-TOKEN': api.getToken(),
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(json => {
                        hashHistory.push('/insertList');
                    });
                } else if (response.status !== ERROR_CODES.NOT_AUTHORISED &&
                    response.status !== ERROR_CODES.ACCESS_DENIED) {
                    response.json().then(json => {
                        try{
                            dispatch(showAlert(json));
                        }catch (e){
                            // nothing to do
                        }

                    });
                } else {
                    dispatch(logout());
                }
            })
            .catch(error => {
                dispatch(showAlert(error));
            })
    }
}

export function updateInsertDate(newDate) {
    let date = api.formatStringToMoment(newDate);

    if(!date || (date.isValid() && date.isAfter(moment()))){
        return {
            type: UPDATE_INSERT_DATE,
            payload: newDate
        }
    }
    return {
        type: UPDATE_INSERT_DATE_FAILURE
    }

}

export function updateInsertFile(newFile) {
    if(newFile){
        return {
            type: UPDATE_INSERT_FILE,
            payload: newFile
        }
    }
    return {
        type: UPDATE_INSERT_FILE_FAILURE
    }
}

export function cleanInsertform(){
    return {
        type: REQUEST_CLEAN_FORM
    }
}

function validateInsertRequest(request, dispatch) {
    let scheduleDate = api.formatStringToMoment(request.scheduleDate);
    if(scheduleDate.isAfter(moment())){
        return true;
    }
    dispatch(showAlert({
        message: i18n.getTextWithParam(MESSAGE_CODE.CODE_BAD_DATETIME_SCHEDULE,request.id),
        severity: api.ERROR_SEVERITY.WARNING,
    }));

    return false;
}
