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
import { fetchInsertsRequests,
    saveTableConfiguration,
    updateInsertRequest} from './InsertActions';
import { BootstrapTable, TableHeaderColumn } from 'react-bootstrap-table';
import DateTimeEditor from '../components/fields/DateTimeEditor';
import {ButtonGroup ,Button, Glyphicon,
    Label} from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import * as api from '../config/api';
import TextWithPopover from '../components/fields/TextWithPopover';

const createDateTimeEditor = (onUpdate, props) => (<DateTimeEditor onUpdate={ onUpdate} {...props}/>);
@connect((store) => {
    return {
        insertRequests: store.insertList.insertRequests,
        tableConfig : store.insertList.tableConfig,
        configuration: store.configuration
    };
})
export default class Inserts extends React.Component {

    componentDidMount() {
        this.props.dispatch(fetchInsertsRequests())
    };

    onBeforeSaveCell = (row, cellName, cellValue) => {
        if(row[cellName]!==cellValue){
            let newRow = {...row};
            newRow[cellName]=cellValue;
            this.props.dispatch(updateInsertRequest(newRow));
        }
        return false;
    };
    
    linkLogFormatter = (cell, row, i18n) => {
        let links = '';
        if(row.successLogPath){
            links = '<a href="'+api.urls.reports+row.successLogPath+'">' + i18n.texts['label.inserts.list.ok.path']+ '</a>';
        }
        if(links!=='' && row.errorLogPath){
            links += ', ';
        }
        if(row.errorLogPath){
            links += '<a href="'+api.urls.reports+row.errorLogPath+'">' + i18n.texts['label.inserts.list.error.path'] + '</a>'
        }
        return links;
    };
    
    actionsFormatter = (cell, row, i18n) => {
        let path = '/insertDetail/'+ row.id;
        return (
            <ButtonGroup>
                <LinkContainer to={path}>
                    <Button title={i18n.texts['label.insert.detail.title']}><Glyphicon glyph="list-alt" /></Button>
                </LinkContainer>
            </ButtonGroup>
        );
    };


    statusFormatter = (cell, row, i18n) => {
        let style = api.getStyleInsertStatus(cell);
        return (<Label bsStyle={style}>{i18n.texts[cell]}</Label>);
    };

    pathFormatter = (cell, row) => {
        let urlFile = api.urls.metadata+row.id+'/'+cell;
        if(cell.length>40){
            let shortText = '...' + cell.substring(cell.length-40);
            return (
                <TextWithPopover id={row.id} tooltip={cell}>
                    <a href={urlFile}>{shortText}</a>
                </TextWithPopover>);
        }
        return (<a href={urlFile}>{cell}</a>);
    };
    
    scheduleDataFormat = (cell, row) =>{
        if(row.status === api.INSERT_REQUEST_STATUS.NOT_STARTED){
            return (
                <Button ref="inputRef" className="selected-date-range-btn" style={{ width: '100%' }}>
                    <div className="pull-left">
                        {api.formatStringToShort(cell)}
                    </div>
                    <div className="pull-right">
                        <Glyphicon glyph="chevron-down"/>
                    </div>
                </Button>
            );
        }else{
            return (<span>{api.formatStringToShort(cell)}</span>);
        }

    };

    onSortChange = (sortName, sortOrder) => {
        this.props.dispatch(saveTableConfiguration(sortName, sortOrder));
    };
    
    render() {
        const { insertRequests } = this.props;
        const i18n = this.props.configuration.i18n;
        const config = this.props.tableConfig;
        
        let cellEditProp = {
            mode: 'click',
            blurToSave: true,
            beforeSaveCell: this.onBeforeSaveCell,
            nonEditableRows: function () {
                return insertRequests.filter(p => p.status !== api.INSERT_REQUEST_STATUS.NOT_STARTED)
                    .map(p => p.id);
            }
        };
        
        let sortName = 'scheduleDate';
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
                <h1>{i18n.texts['label.inserts.list.title']}</h1>
                <BootstrapTable data={ insertRequests } 
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
                        isKey={ true }>
                        {i18n.texts['label.id']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        width={api.FIELD_SIZES.date + ""}
                        dataField='scheduleDate'
                        dataFormat={this.scheduleDataFormat}
                        customEditor={{getElement:createDateTimeEditor }}
                        sortFunc={ api.sortDateTimeFunction }
                        dataSort={true}>
                        {i18n.texts['label.insert.schedule.date']}
                    </TableHeaderColumn>
                    <TableHeaderColumn 
                        width={api.FIELD_SIZES.date + ""} 
                        dataField='startDate'
                        dataFormat={api.formatStringToShort}
                        sortFunc={ api.sortDateTimeFunction }
                        dataSort={true}>
                        {i18n.texts['label.insert.start.date']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        width={api.FIELD_SIZES.date + ""}
                        dataField='endDate'
                        sortFunc={ api.sortDateTimeFunction }
                        dataFormat={api.formatStringToShort}
                        dataSort={true}>
                        {i18n.texts['label.insert.end.date']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        dataField='fileName'
                        dataSort={true}
                        editable={false}
                        dataFormat={this.pathFormatter}>
                        {i18n.texts['label.insert.filename']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        width="100"
                        dataField='successLogPath'
                        dataFormat={this.linkLogFormatter} 
                        formatExtraData={i18n}
                    >{i18n.texts['label.logs']}</TableHeaderColumn>
                    <TableHeaderColumn 
                        width="150" 
                        dataField='status'
                        dataSort={true}
                        dataFormat={this.statusFormatter}
                        formatExtraData={i18n}
                        filter={ { type: 'SelectFilter', options: api.insertRequestStatusTranslations() } }
                        dataAlign='center'
                    >{i18n.texts['label.status']}
                    </TableHeaderColumn>
                    <TableHeaderColumn
                        width="150"
                        dataFormat={this.actionsFormatter}
                        formatExtraData={i18n}
                        editable={false}
                        dataAlign='center'
                    >{i18n.texts['label.actions']}</TableHeaderColumn>
                </BootstrapTable>
            </div>
        );
    }
}
