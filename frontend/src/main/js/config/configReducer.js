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

import {SHOW_ALERT,
    REMOVE_ALERT,
    FETCH_I18N,
    FETCH_I18N_SUCCESS,
    FETCH_I18N_FAIL,
    USER_LOGOUT,
    USER_LOGIN
} from './configActions';
import * as api from './api';

const INITIAL_STATE ={
    alert:{
        severity:api.ERROR_SEVERITY.INFORMATION,
        code: 0,
        message: '',
        status: 0,
        details: null,
        enabled: false
    },
    i18n:{
        loading: false,
        error: null,
        texts: {}
    },
    user: {
        logged: false,
        name: ''
    }
};

export default function configReducer(state= INITIAL_STATE, action) {

    switch (action.type) {
        case SHOW_ALERT: {
            return {
                ...state, 
                alert: {
                    ...state.alert,
                    severity: action.payload.severity?action.payload.severity:api.ERROR_SEVERITY.INFORMATION,
                    status: action.payload.status?action.payload.status:0,
                    message: action.payload.message?action.payload.message:'',
                    details: action.payload.details?action.payload.details:action.payload.stack?action.payload.stack:'',
                    code: action.payload.code?action.payload.code:0,
                    enabled:true
                } 
            }
        }
        case REMOVE_ALERT: {
            return {
                ...state,
                alert:{
                    severity:api.ERROR_SEVERITY.INFORMATION,
                    code: 0,
                    message: '',
                    status: 0,
                    details: null,
                    enabled: false
                }
            }
        }
        case FETCH_I18N: {
            return {
                ...state,
                i18n: { error: null, loading: true}
            }
        }
        case FETCH_I18N_SUCCESS: {
            return {
                ...state,
                i18n:{
                    error: null,
                    loading: false,
                    texts:action.payload
                }
            }
        }
        case FETCH_I18N_FAIL:{
            return {
                ...state,
                i18n: {
                    error: action.payload,
                    loading: false,
                    texts:{}
                }
            }
        }
        case USER_LOGIN:{
            return {
                ...state,
                user: {
                    logged:true,
                    name: action.payload
                }
            }
        }
        case USER_LOGOUT:{
            return {
                ...state,
                user: {
                    logged:false,
                    name: ''
                }
            }
        }
        default:
            return state;
            
    }

    return state
}