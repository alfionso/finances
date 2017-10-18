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

export default function reducer(state={
    user: {
        id: null,
        name: null,
        age: null,
    },
    fetching: false,
    fetched: false,
    error: null,
}, action) {

    switch (action.type) {
        case "FETCH_USER": {
            return {...state, fetching: true}
        }
        case "FETCH_USER_REJECTED": {
            return {...state, fetching: false, error: action.payload}
        }
        case "FETCH_USER_FULFILLED": {
            return {
                ...state,
                fetching: false,
                fetched: true,
                user: action.payload,
            }
        }
        case "SET_USER_NAME": {
            return {
                ...state,
                user: {...state.user, name: action.payload},
            }
        }
        case "SET_USER_AGE": {
            return {
                ...state,
                user: {...state.user, age: action.payload},
            }
        }
    }

    return state
}