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
import { Nav, NavItem } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { connect } from 'react-redux';
import {logout} from '../config/configActions';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class Menu extends React.Component {

    doLogout = () =>{
        this.props.dispatch(logout());
    };
    
    render() {
        const i18n = this.props.configuration.i18n;
        return (
            <Nav pullRight>
                <LinkContainer to="/insertForm">
                    <NavItem eventKey={1}><span>{i18n.texts['label.menu.insert.form.title']}</span></NavItem>
                </LinkContainer>
                <LinkContainer to="/transactionList">
                    <NavItem eventKey={2}><span>{i18n.texts['label.menu.transactions.title']}</span></NavItem>
                </LinkContainer>
                <LinkContainer to="/insertList">
                    <NavItem eventKey={3}><span>{i18n.texts['label.menu.inserts.title']}</span></NavItem>
                </LinkContainer>
                <NavItem eventKey={4} onClick={this.doLogout}><span>{i18n.texts['label.logout.link.text']}</span></NavItem>
            </Nav>
            
        )
    }
}