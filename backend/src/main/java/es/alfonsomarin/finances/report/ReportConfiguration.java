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

package es.alfonsomarin.finances.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The type Report configuration.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
public class ReportConfiguration {

    /**
     * The constant LOG_ERROR_FILENAME.
     */
    public static final String LOG_ERROR_FILENAME = "failure.csv";
    /**
     * The constant LOG_SUCCESSFULL_FILENAME.
     */
    public static final String LOG_SUCCESSFULL_FILENAME = "success.csv";
    /**
     * The constant LOG_UPLOAD_FILENAME.
     */
    public static final String LOG_UPLOAD_FILENAME = "sumary.log";
    /**
     * The constant REPORT_FOLDER_NAME.
     */
    public static final String REPORT_FOLDER_NAME = "report";
    /**
     * The constant VALIDATION_FOLDER_NAME.
     */
    public static final String VALIDATION_FOLDER_NAME = "collection";
    /**
     * The constant INSERT_FOLDER_NAME.
     */
    public static final String INSERT_FOLDER_NAME = "insert";

    @Value("${report.log.folder:./static}")
    private String logFolder;
    
    @Value("${metainformation.separator:,}")
    private String csvSeparator;

    @Value("${metainformation.date.format:''}")
    private String dateFormat;

    @Value("${metainformation.file.header.lines:1}")
    private int linesHeader;

    /**
     * Sets log folder.
     *
     * @param logFolder the log folder
     */
    public void setLogFolder(String logFolder) {
        this.logFolder = logFolder;
    }

    /**
     * Sets csv separator.
     *
     * @param csvSeparator the csv separator
     */
    public void setCsvSeparator(String csvSeparator) {
        this.csvSeparator = csvSeparator;
    }

    /**
     * Sets date format.
     *
     * @param dateFormat the date format
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * Is has header boolean.
     *
     * @return the boolean
     */
    public int getLinesHeader() {
        return linesHeader;
    }

    /**
     * Sets has header.
     *
     * @param linesHeader the has header
     */
    public void setLinesHeader(int linesHeader) {
        this.linesHeader = linesHeader;
    }

    /**
     * Gets log folder.
     *
     * @return the log folder
     */
    public String getLogFolder() {
        return logFolder;
    }

    /**
     * Get csv separator string.
     *
     * @return the string
     */
    public String getCsvSeparator(){
        return csvSeparator;
    }

    /**
     * Get date format string.
     *
     * @return the string
     */
    public String getDateFormat(){
        return dateFormat;
    }

}
