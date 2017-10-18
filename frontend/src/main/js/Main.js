/*
 * Copyright 2016-2007 Alfonso Marin Lopez.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {hashHistory, Router} from 'react-router';
import cookie from 'react-cookie';
import routes from './routes';
import store from './store';
import * as api from './config/api';

class Main extends React.Component{
    constructor(props){
        super(props);
        // On start application, this token is require to make calls security
        // The cookie is save and use it in each rest call
        if (!cookie.load(api.CONFIG.CSRF_TOKEN)) {
            fetch(api.urls.config.csrf+ '?' + new Date().getTime(), {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Accept': '*/*',
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                }
            }).then((data) => {});
        }
    }

    render(){
        return (
            <Provider store={store}>
                <Router history={hashHistory} routes={routes}/>
            </Provider>
        );
    }
}

ReactDOM.render(<Main/>, document.getElementById('app'));
/*
const unloadPage = () => {
    sessionStorage.removeItem('Target');
    localStorage.removeItem('tokenData');
};

window.onunload = unloadPage;*/