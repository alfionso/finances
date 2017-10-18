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
import * as api from '../../config/api';
import { connect } from 'react-redux';
import css from 'bootstrap-daterangepicker/daterangepicker.css';
import moment from 'moment';
import DatetimeRangePicker from 'react-bootstrap-datetimerangepicker';
import { 
    Button,
    Glyphicon,
    FormControl
} from 'react-bootstrap';

@connect((store) => {
    return {
        configuration: store.configuration
    };
})
export default class DateTimeEditor extends React.Component {
    constructor(props) {
        super(props);
    }
    
    handleChangeDate = (event, picker) => {
        this.props.onUpdate(api.formatDateToServer(picker.startDate));
    };

    focus = ()=> {
        ReactDOM.findDOMNode(this.inputRef).focus();
    };

    render() {
        const i18n = this.props.configuration.i18n;
        let dateFormat = (<span>{api.formatStringToShort(this.props.defaultValue)}</span>);
        let locale = {
            format: api.DATETIME_SHORT_FORMAT,
            separator: ' - ',
            applyLabel: i18n.texts['label.apply'],
            cancelLabel: i18n.texts['label.cancel'],
            daysOfWeek: moment.weekdaysMin(),
            monthNames: moment.monthsShort(),
            firstDay: 1,
        };
        let starDate = moment();
        if(this.props.defaultValue){
            starDate = api.formatStringToMoment(this.props.defaultValue);
        }
        
        return (
            <span>               
                <DatetimeRangePicker
                    timePicker
                    timePicker24Hour
                    singleDatePicker
                    showDropdowns
                    locale={locale}
                    onApply={this.handleChangeDate}
                    startDate={starDate}
                    inputRef={ref => { this.inputRef = ref; }}
                >
                    <Button className="selected-date-range-btn" style={{ width: '100%' }}>
                        <div className="pull-left">
                            {dateFormat}
                        </div>
                        <div className="pull-right">
                            <Glyphicon glyph="chevron-down"/>
                        </div>
                    </Button>
                </DatetimeRangePicker>
            </span>
        );
    }
}
