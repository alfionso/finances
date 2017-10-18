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

import moment from 'moment';
import cookie from 'react-cookie';
import {showAlert, logout} from './configActions';
import * as i18n from '../config/i18n';

const ROOT_API_URL =  'api/';
const ROOT_URL =  '';
const ROOT_WS_URL = 'ws://' + location.host + location.pathname + 'wscommunication';
const ROOT_URL_RESOURCES = '';

const toAPIUrl = u => ROOT_API_URL + u;
const toUrl = u => ROOT_URL + u;

export const CONFIG = {
    CSRF_TOKEN : 'XSRF-TOKEN'
};

export const DATETIME_FORMAT = 'YYYY-MM-DDTHH:mm:ss.SSSZ';
export const DATETIME_SHORT_FORMAT = 'DD/MM/YYYY HH:mm';
export const DATE_SHORT_FORMAT = 'DD/MM/YYYY';


export function withParams(u, params) {
    let indexParams = 0;
    let result = u;
    while (result.indexOf('%') >= 0) {
        result = result.replace('%', params[indexParams]);
        indexParams++;
    }
    return result;
}

export const ERROR_SEVERITY = {
    WARNING: 'WARNING',
    INFORMATION: 'INFORMATION',
    ERROR: 'ERROR'
};

export const ERROR_CODES = {
    BAD_REQUEST: 400,
    NOT_AUTHORISED: 401,
    ACCESS_DENIED: 403,
    NOT_FOUND: 404
};

export const MESSAGE_CODE = {
    CODE_BAD_DATETIME_SCHEDULE: 'message.500',
    CODE_SCHEDULE_MESSAGE_CONFIRMATION: 'message.503'
};

export const WS_TYPE ={
    TRANSACTION_UPDATE : 'TRANSACTION_UPDATE',
    TRANSACTION_REMOVE : 'TRANSACTION_REMOVE',
    INSERT_UPDATE : 'INSERT_UPDATE',
    INSERT_REMOVE: 'INSERT_REMOVE'
};

export const FIELD_SIZES = {
    date : 170,
    id : 40
};


// Insert request utilities
export const INSERT_REQUEST_STATUS = {
    IN_PROGRESS : 'IN_PROGRESS',
    NOT_STARTED : 'NOT_STARTED',
    SUCCESS : 'SUCCESS',
    FAILED: 'FAILED',
    PAUSED : 'PAUSED',
    ABORTED : 'ABORTED',
    DELETED: 'DELETED'
};
export const insertRequestStatusTranslations = () => {
    let statusTranslated = {};
    for(let status in INSERT_REQUEST_STATUS){
        statusTranslated[status] = i18n.getText(status);
    }
    return statusTranslated;
};

export const getStyleInsertStatus = (status) => {
    let style;
    switch (status){
        case INSERT_REQUEST_STATUS.ABORTED:
        case INSERT_REQUEST_STATUS.MISSED_SCHEDULE:
        case INSERT_REQUEST_STATUS.DELETE:
            style='warning';
            break;
        case INSERT_REQUEST_STATUS.FAILED:
            style='danger';
            break;
        case INSERT_REQUEST_STATUS.IN_PROGRESS:
            style='primary';
            break;
        case INSERT_REQUEST_STATUS.NOT_STARTED:
        case INSERT_REQUEST_STATUS.PAUSED:
            style='default';
            break;
        case INSERT_REQUEST_STATUS.SUCCESS:
            style='success';
            break;
        default:
            style = 'default';
            break;
    }
    return style;
};

export let urls = {
    transaction : {
        all : toAPIUrl('transaction'),
        update : toAPIUrl('transaction/%'),
        validation : toAPIUrl('transaction/%/validation'),
        delete : toAPIUrl('transaction/%')
    },
    insert:{
        all: toAPIUrl('insert'),
        get: toAPIUrl('insert/%'),
        update: toAPIUrl('insert/%'),
        create : toAPIUrl('insert'),
        start: toAPIUrl('insert/%/start'),
        pause: toAPIUrl('insert/%/pause'),
        resume: toAPIUrl('insert/%/resume'),
        abort: toAPIUrl('insert/%/abort')
    },
    config:{
        i18n: toAPIUrl('config/messages'),
        metadataTree: toAPIUrl('config/metadata/tree'),
        csrf: toUrl('csrf'),
        login: toUrl('login'),
        logout: toUrl('')
    },
    reports : ROOT_URL_RESOURCES,
    metadata: ROOT_URL_RESOURCES + 'metadata/',
    
    ws: ROOT_WS_URL
};


export const formatDateToServer = date =>{
    // date is a moment object
    return date.format(DATETIME_FORMAT);
};

export const formatStringToShort = string =>{
    const currentDate = moment(string, DATETIME_FORMAT);
    if(currentDate.isValid()){
        return currentDate.format(DATETIME_SHORT_FORMAT);
    }else return '';
    
};

export const formatStringDateToShort = string =>{
    const currentDate = moment(string, DATETIME_FORMAT);
    if(currentDate.isValid()){
        return currentDate.format(DATE_SHORT_FORMAT);
    }else return '';
};

export const formatStringToMoment = string =>{
    return moment(string, DATETIME_FORMAT);
};
export function sortDateTimeFunction(a, b, order, sortField) {
    const aTime = formatStringToMoment(a[sortField]).valueOf() || 0;
    const bTime = formatStringToMoment(b[sortField]).valueOf() || 0;
    if (order === 'desc') {
        return bTime - aTime;
    } else {
        return aTime - bTime;
    }
}

export let readUserData = () => {
    if (!localStorage.tokenData) {
        return {
            'email': '',
            'fullName': '',
            'id': 0,
            'notificationEnabled': true,
            'officeCode': '',
            'password': '',
            'position': '',
            'roles': [],
            'username': ''
        };
    } else {
        return JSON.parse(localStorage.getItem('tokenData'));
    }
};

/**
 * returns the token
 */
export const getToken = () =>{
    return cookie.load(CONFIG.CSRF_TOKEN);
}; 

// ################ FETCH CUSTOMIZATION
export const get = (url,dispatch , config = {}) => new Promise((resolve, reject) =>{
       
        fetch(url+ '?' + new Date().getTime(), {
            method: 'GET',
            credentials: 'include',
            ...config,
            headers: {
                'Accept': '*/*',
                'X-CSRF-TOKEN': getToken(),
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(response => {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.indexOf('application/json') !== -1) {
                    if (response.ok) {
                        response.json().then(json => resolve(json));
                    } else if (response.status !== ERROR_CODES.NOT_AUTHORISED &&
                                response.status !== ERROR_CODES.ACCESS_DENIED) {
                        response.json().then(json => {
                            dispatch(showAlert(json));
                            reject(json);
                        });
                    } else {
                        dispatch(logout());
                    }
                } else {
                    response.text().then((htmlContent) => {
                        if (htmlContent.indexOf('http') === 0) {
                            window.exitingNow = true;
                            document.location.href = htmlContent;
                        } else {
                            resolve(htmlContent);
                        }
                    });
                }
            })
            .catch(error => {
                dispatch(showAlert(error));
                reject(error)
            })  
});

export const post = (url, data, dispatch, config = {}) => new Promise((resolve, reject) =>
        fetch(url, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify(data),
            ...config,
            headers: {
                'X-CSRF-TOKEN': getToken(),
                'X-Requested-With': 'XMLHttpRequest',
                'content-type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(json => resolve(json));
                } else if (response.status !== ERROR_CODES.NOT_AUTHORISED &&
                    response.status !== ERROR_CODES.ACCESS_DENIED) {
                    response.json().then(json => {
                        try{
                            dispatch(showAlert(json));
                        }catch (e){
                            // nothing to do
                        }
                        
                        reject(json);
                    });
                } else {
                    dispatch(logout());
                }
            })
            .catch(error => {
                dispatch(showAlert(error));
                reject(error);
            })
    );

export const put = (url, data, dispatch) => new Promise((resolve, reject) =>
    fetch(url, {
        method: 'PUT',
        credentials: 'include',
        body: JSON.stringify(data),
        headers: {
            'X-CSRF-TOKEN': getToken(),
            'X-Requested-With': 'XMLHttpRequest',
            'content-type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                response.json().then(json => resolve(json));
            } else if (response.status !== ERROR_CODES.NOT_AUTHORISED &&
                response.status !== ERROR_CODES.ACCESS_DENIED) {
                response.json().then(json => {
                    dispatch(showAlert(json));
                    reject(json);
                });
            } else {
                dispatch(logout());
            }
        })
        .catch(error => {
            dispatch(showAlert(error));
            reject(error);
        })
);

export const del = (url, dispatch) => new Promise((resolve, reject) =>
    fetch(url, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
            'X-CSRF-TOKEN': getToken(),
            'X-Requested-With': 'XMLHttpRequest',
            'content-type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                resolve();
            } else if (response.status !== ERROR_CODES.NOT_AUTHORISED &&
                response.status !== ERROR_CODES.ACCESS_DENIED) {
                response.json().then(json => {
                    dispatch(showAlert(json));
                    reject(json);
                });
            } else {
                dispatch(logout());
            }
        })
        .catch(error => {
            dispatch(showAlert(error));
            reject(error);
        })
);