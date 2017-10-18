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
import { hashHistory } from 'react-router';
import { fetchTransaction, 
    updateTransaction,
    deleteTransaction,
    saveTableConfiguration} from './TransactionListActions';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import {ButtonGroup ,Button, Glyphicon,
    Modal, Label} from 'react-bootstrap';
import DateTimeEditor from '../components/fields/DateTimeEditor';
import TextWithPopover from '../components/fields/TextWithPopover';
import * as api from '../config/api';

const createDateTimeEditor = (onUpdate, props) => (<DateTimeEditor onUpdate={ onUpdate} {...props}/>);
@connect((store) => {
    return {
        transactions: store.transactionList.transactions,
        tableConfig : store.transactionList.tableConfig,
        configuration: store.configuration
    };
})
export default class TransactionList extends React.Component {
    constructor(props){
        super(props);
        this.state = {
            showModal : false,
            id : '',
            fileName : ''
        }
    }

    componentDidMount() {
        this.props.dispatch(fetchTransaction())
    };

    onBeforeSaveCell = (row, cellName, cellValue) => {
        if(row[cellName]!==cellValue){
            let newRow = {...row};
            newRow[cellName]=cellValue;
            this.props.dispatch(updateTransaction(newRow)); 
        }
        return false;
    };
    
    actionsFormatter = (cell, row, i18n) => {
        
        let removeButton = (<Button onClick={() => this.openDeleteModal(row.id, row.description) }><Glyphicon glyph='remove' /></Button>);
        
        return (
            <ButtonGroup>
                {removeButton}
            </ButtonGroup>
        );
    };
    
    
    idFormatter = (cell, row) =>{
        return (<b>{cell}</b>);
    };
    
    closeDeleteModal = () => {
        this.setState({ showModal : false});
    };
    
    deleteTransaction = () => {
        this.closeDeleteModal();
        this.props.dispatch(deleteTransaction(this.state.id));
    };
    
    openDeleteModal = (id, fileName) => {
        this.setState({showModal:true, id : id, fileName : fileName});
    };
    
    createDeleteModal = (i18n) =>{
        return (
            <Modal show={this.state.showModal} onHide={this.closeDeleteModal}>
            <Modal.Header closeButton>
                <Modal.Title>{i18n.texts['label.transaction.list.modal.delete.title']}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <h4>{i18n.texts['label.transaction.list.modal.delete.body']}</h4>
                <ul>
                    <li>{i18n.texts['label.id']}: {this.state.id}</li>
                    <li>{i18n.texts['label.transaction.list.modal.delete.file.name']}: {this.state.fileName}</li>
                </ul>

            </Modal.Body>
            <Modal.Footer>
                <Button bsStyle='link' onClick={this.closeDeleteModal}>{i18n.texts['label.close']}</Button>
                <Button bsStyle='warning' onClick={this.deleteTransaction}>{i18n.texts['label.delete']}</Button>
            </Modal.Footer>
        </Modal>
        )
    };
    
    dataFormat = (cell, row) =>{
        return (<span>{api.formatStringDateToShort(cell)}</span>);
    };
    
    valueFormat = (cell, row) => {
        if(cell){
            return `${cell.toFixed(2)} <i class='glyphicon glyphicon-euro'></i>`;    
        }else{
            return '-';
        }
        
    };
    
    onSortChange = (sortName, sortOrder) => {
        this.props.dispatch(saveTableConfiguration(sortName, sortOrder));
    };
    
    render() {
        const { transactions } = this.props;
        const config = this.props.tableConfig;
        const i18n = this.props.configuration.i18n;
        
        let cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.onBeforeSaveCell,
        };
        
        let sortName = 'date';
        let sortOrder = 'asc';
        if(config){
            sortName = config.field;
            sortOrder = config.order;
        }
        let tableOptions = {
            defaultSortName: sortName,
            defaultSortOrder: sortOrder, 
            clearSearch: true,
            onSortChange: this.onSortChange
        };
        
        return (
            <div>
                <h1>{i18n.texts['label.transaction.list.title']}</h1>
               
                <BootstrapTable data={ transactions } 
                                bordered={ false } 
                                striped 
                                hover
                                search={true}
                                cellEdit={ cellEditProp }
                                options={tableOptions}
                                tableHeaderClass='finances-table-header'>
                    <TableHeaderColumn 
                        width={api.FIELD_SIZES.id + ""}
                        dataField='id' 
                        dataFormat={this.idFormatter}
                        isKey={ true }>
                        {i18n.texts['label.id']}
                    </TableHeaderColumn>
                    <TableHeaderColumn 
                        width={api.FIELD_SIZES.date + ""}
                        dataField='date'
                        customEditor={{getElement:createDateTimeEditor }}
                        dataFormat={this.dataFormat}
                        dataSort={true}
                        sortFunc={ api.sortDateTimeFunction }>
                        {i18n.texts['label.transaction.date']}
                    </TableHeaderColumn>
                    
                    <TableHeaderColumn 
                        dataField='description'
                        dataSort={true}
                        editable={false}>
                        {i18n.texts['label.transaction.description']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        dataField='value'
                        dataSort={true}
                        dataFormat={this.valueFormat}
                        editable={false}>
                        {i18n.texts['label.transaction.value']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        width="150"
                        dataFormat={this.actionsFormatter}
                        formatExtraData={i18n}
                        editable={false}
                        dataAlign='center'
                    >{i18n.texts['label.actions']}</TableHeaderColumn>
                </BootstrapTable>
                {this.createDeleteModal(i18n)}
                
            </div>
        );
    }
}
