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

import {
    FETCH_INSERT,
    FETCH_INSERT_SUCCESS,
    FETCH_INSERT_FAILURE,
    UPDATE_INSERT,
    UPDATE_INSERT_FAILURE,
    UPDATE_INSERT_DATE,
    UPDATE_INSERT_DATE_FAILURE,
    UPDATE_INSERT_PATH,
    UPDATE_INSERT_PATH_FAILURE,
    UPDATE_INSERT_FILE,
    UPDATE_INSERT_FILE_FAILURE,
    UPDATE_TO_PAUSE,
    REQUEST_CLEAN_FORM} from '../insert/InsertActions';
import * as api from '../config/api'

const INITIAL_STATE ={
    request:{
        id: 0,
        scheduleDate: '',
        startDate: '',
        endDate: '',
        fileName:'',
        status: api.INSERT_REQUEST_STATUS.NOT_STARTED,
        statusCode: '',
        summary: '',
        successLogPath: '',
        errorLogPath: '',
        file: null,
        totalLines:0,
        successLines:0,
        errorLines:0
    },
    validation:{
        date: true,
        file: false
    },
    loading : false,
    error: null,
    sendingPause:false
};

export default function reducer(state=INITIAL_STATE, action) {

    switch (action.type) {
        case FETCH_INSERT: {
            return {
                ...state,
                loading: true,
                error: null,
                sendingPause:false
            }
        }
        case FETCH_INSERT_SUCCESS: {
            return {
                ...state,
                request: action.payload,
                validation:{date:true},
                loading: false,
                error: null,
                sendingPause: false
            }
        }
        case FETCH_INSERT_FAILURE:{
            return {
                ...state,
                loading:false,
                error: action.payload,
                sendingPause:false
            }
        }
        case UPDATE_INSERT: {
            return {
                ...state,
                request: state.request.id === action.payload.id ? action.payload : state.request,
                validation:{date:true},
                loading: false,
                error: null,
                sendingPause:false
            }
        }
        case UPDATE_INSERT_FAILURE: {
            return {
                ...state,
                request: INITIAL_STATE.request,
                validation: INITIAL_STATE.validation,
                loading: false,
                error: action.payload,
                sendingPause:false
            }
        }
        case UPDATE_INSERT_DATE:{
            return {
                ... state,
                request: {...state.request, scheduleDate:action.payload},
                validation: {...state.validation, date:true  },
                loading: false,
                error: null,
                sendingPause:false
            }
        }
        case UPDATE_INSERT_DATE_FAILURE: {
            return {
                ... state,
                request: {...state.request, scheduleDate: ''},
                validation: {...state.validation, date:false  },
                loading: false,
                error: null,
                sendingPause: false
            }
        }
        case UPDATE_INSERT_PATH:{
            return {
                ... state,
                request: {...state.request ,fileName:action.payload},
                validation: {...state.validation, path:true  },
                loading: false,
                error: null,
                sendingPause: false
            }
        }
        case UPDATE_INSERT_PATH_FAILURE: {
            return {
                ... state,
                request: {...state.request, fileName: ''},
                validation: {...state.validation, path:false  },
                loading: false,
                error: null,
                sendingPause:false
            }
        }
        case UPDATE_INSERT_FILE:{
            return {
                ... state,
                request: {...state.request, file :action.payload, fileName:action.payload.name },
                validation: {...state.validation, file:true  },
                loading: false,
                error: null,
                sendingPause: false
            }
        }
        case UPDATE_INSERT_FILE_FAILURE: {
            return {
                ... state,
                request: {...state.request, file: null},
                validation: {...state.validation, file:false  },
                loading: false,
                error: null,
                sendingPause: false
            }
        }
        case UPDATE_TO_PAUSE:{
            return {
                ...state,
                request: {...state.request},
                validation: {...state.validation},
                loading : false,
                error: null,
                sendingPause:action.payload
            }
        }
        case REQUEST_CLEAN_FORM:{
            return INITIAL_STATE;
        }
    }

    return state
}