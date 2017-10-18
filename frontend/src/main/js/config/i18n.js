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

/*
There are two places where the i18n is store.
- In the configuration reducer, this is for component translations
- Window.i18n, this is for actions and methods that don't have access to a redux.
 */

export let getText = (key) => {
    let _key = key || null;

    if (_key && window.i18n && window.i18n[_key]) {
        return window.i18n[_key];
    } else {
        return _key;
    }
};

export let getTextWithParam = (key, ...params) => {
        let message = getText(key);
        let i;
        for(i=0; i<params.length; i++){
            let nextParam = '{'+i+'}';
            message = message.replace(nextParam, params[i]);
        }

        return message;
};