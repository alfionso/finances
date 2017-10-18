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

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class Footer extends React.Component {
    render() {
        const i18n = this.props.configuration.i18n;
        return (
            <Navbar fixedBottom>
                <Navbar.Header>
                    <Navbar.Brand>
                        {i18n.texts['label.footer.release']}
                    </Navbar.Brand>
                </Navbar.Header>
            </Navbar>
        );
    }
}