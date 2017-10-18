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

import {FETCH_TRANSACTION,
    FETCH_TRANSACTION_SUCCESS,
    FETCH_TRANSACTION_FAILURE,
    TRANSACTION_UPDATE,
    TRANSACTION_REMOVE,
    TABLE_SAVE_CONFIGURATION} from './TransactionListActions';

const INITIAL_STATE ={
    transactions: [],
    loading: false,
    error: null,
    tableConfig: {
        field: 'date',
        order: 'desc'
    }
};

export default function reducer(state= INITIAL_STATE, action) {
    switch (action.type) {
        case FETCH_TRANSACTION: {
            return {...state, error: null, loading: true}
        }
        case FETCH_TRANSACTION_FAILURE: {
            return {
                ...state,
                transactions: [], 
                loading: false, 
                error: action.payload.message
            }
        }
        case FETCH_TRANSACTION_SUCCESS: {
            return {
                ...state,
                error: null,
                loading: false,
                transactions: action.payload,
            }
        }
        case "ADD_TRANSACTION_REQUEST": {
            return {
                ...state,
                transactions: [...state.transactions, action.payload],
            }
        }
        case TRANSACTION_UPDATE: {
            return {
                ...state,
                transactions: state.transactions.map(
                    (transaction) => transaction.id === action.payload.id ?
                        action.payload
                        : transaction
                )
            }
        }
        
        case TABLE_SAVE_CONFIGURATION:{
            return {
                ...state,
                tableConfig:{
                    field: action.payload.field,
                    order: action.payload.order
                }
            }
        }
        
        case TRANSACTION_REMOVE: {
            return {
                ...state,
                transactions: state.transactions.filter(
                    (transaction) => transaction.id !== action.payload
                )
            }
        }
        default:
            return state;
            
    }

    return state
}