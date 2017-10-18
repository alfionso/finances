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

import {FETCH_INSERTS,
    FETCH_INSERTS_FAILURE,
    FETCH_INSERTS_SUCCESS,
    UPDATE_INSERT_LIST,
    INSERT_TABLE_SAVE_CONFIGURATION} from './InsertActions';

const INITIAL_STATE ={
    insertRequests: [],
    loading: false,
    error: null,
    tableConfig: {
        field: 'scheduleDate',
        status: null,
        order: 'desc'
    }
};

export default function insertReducer(state= INITIAL_STATE, action) {

    switch (action.type) {
        case FETCH_INSERTS: {
            return {...state, error: null, loading: true}
        }
        case FETCH_INSERTS_FAILURE: {
            return {
                ...state,
                insertRequests: [], 
                loading: false, 
                error: action.payload.message
            }
        }
        case FETCH_INSERTS_SUCCESS: {
            return {
                ...state,
                error: null,
                loading: false,
                insertRequests: action.payload,
            }
        }
        case UPDATE_INSERT_LIST: {
            return {
                ...state,
                insertRequests: state.insertRequests.map(
                    (insert) => insert.id === action.payload.id ? 
                        action.payload
                        : insert
                )
            }
        }
        case INSERT_TABLE_SAVE_CONFIGURATION:{
            return {
                ...state,
                tableConfig:{
                    field: action.payload.field,
                    order: action.payload.order
                }
            }
        }
        default:
            return state;
    }
    return state
}