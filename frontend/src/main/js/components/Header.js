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
import { Navbar } from 'react-bootstrap';
import { connect } from 'react-redux';
import Menu from './Menu';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class Header extends React.Component {

    render() {
        const i18n = this.props.configuration.i18n;
        return (
            <Navbar inverse collapseOnSelect fixedTop fluid>
                <Navbar.Header>
                    <Navbar.Brand>
                        <a href="#">{i18n.texts['label.header.title']}</a>
                    </Navbar.Brand>
                    <Navbar.Toggle />
                </Navbar.Header>
                <Navbar.Collapse>
                    <Menu/>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}