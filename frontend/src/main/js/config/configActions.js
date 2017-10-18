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

// request list
export const SHOW_ALERT = 'SHOW_ALERT';
export const REMOVE_ALERT = 'REMOVE_ALERT';
export const FETCH_I18N = 'FETCH_I18N';
export const FETCH_I18N_SUCCESS = 'FETCH_I18N_SUCCESS';
export const FETCH_I18N_FAIL = 'FETCH_I18N_FAIL';
export const USER_LOGIN = 'USER_LOGIN';
export const USER_LOGOUT = 'USER_LOGOUT';



export function showAlert(alert){
    return {
        type:SHOW_ALERT,
        payload: alert
    }
}

export function removeAlert() {
    return {
        type: REMOVE_ALERT
    }
}

export function fetchI18N(){
    return function (dispatch) {
        return api.get(api.urls.config.i18n,dispatch)
            .then((response) => {
                window.i18n = response;
                dispatch({type: FETCH_I18N_SUCCESS, payload: response})
            })
            .catch((error) => {
                dispatch({type: FETCH_I18N_FAIL, payload: error})
            });
    }
}

export function login(username){
    return {
        type: USER_LOGIN,
        payload: username
    }
}

export function logout(){
    return function (dispatch) {
        fetch(api.urls.logout, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'X-CSRF-TOKEN': api.getToken()
            }
        })
            .then(response => {
                localStorage.removeItem('tokenData');
                dispatch({
                    type: USER_LOGOUT
                });
            })
            .catch(error => {
                dispatch({
                    type: USER_LOGOUT
                });
            });
        
    }
}


