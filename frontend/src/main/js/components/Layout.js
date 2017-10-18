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

import React from 'react';
import { connect } from 'react-redux';
import Header from './Header';
import Footer from './Footer';
import AlertMessage from './AlertMessage';
import WSCommunication from '../config/wsCommunication';
import {fetchI18N, login, logout} from '../config/configActions';
import {Grid, Row, Col} from 'react-bootstrap';
import Signin from '../components/Signin';
import * as api from '../config/api';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class Layout extends React.Component {
    constructor(props){
        super(props);
        this.props.dispatch(fetchI18N());
        const usrData = api.readUserData();
        if (!usrData.username) {
            sessionStorage.Target = document.location.hash.replace('#', '').split('?')[0];
            this.props.dispatch(logout());
        }else{
            this.props.dispatch(login(usrData.username));
        }
    }
    
    render() {
        const bodyStyle = {
            paddingTop : '50px',
            paddingBottom : '50px',
            marginRight:   '40px', 
            marginLeft:   '40px'
        };
        let body = (<Signin/>);
        let user = this.props.configuration.user;
        if(user.logged){
            body = (<div>
                <WSCommunication/>
                <Header/>
                <Row>
                    <Col >
                        <div style={bodyStyle} className='container-fluid'>
                            {this.props.children}
                        </div>
                    </Col>
                </Row>
                <Footer/>
            </div>
            );
        }
        return (
            <div>
                <AlertMessage/>
                {body}
            </div>
        );
    }
}
