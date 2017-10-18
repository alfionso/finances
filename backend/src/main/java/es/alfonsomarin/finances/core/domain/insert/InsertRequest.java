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

package es.alfonsomarin.finances.core.domain.insert;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.alfonsomarin.finances.core.ApplicationConfig;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * The type Insert request.
 *
 * @author alfonso.marin.lopez
 */
public class InsertRequest {
    
    private Long id;
    
    private InsertRequestStatus status;
    
    private String statusCode;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = ApplicationConfig.DATETIME_FORMAT)
    @DateTimeFormat(pattern=ApplicationConfig.DATETIME_FORMAT)
    private Date startDate=null;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = ApplicationConfig.DATETIME_FORMAT)
    @DateTimeFormat(pattern=ApplicationConfig.DATETIME_FORMAT)
    private Date endDate=null;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = ApplicationConfig.DATETIME_FORMAT)
    @DateTimeFormat(pattern=ApplicationConfig.DATETIME_FORMAT)
    private Date scheduleDate=null;

    @NotNull(message = MessageCodes.VALIDATION_PATH_MANDATORY)
    private String fileName;
    
    private String summary="";

    private String successLogPath=null;

    private String errorLogPath=null;
    
    private int totalLines;
    private int successLines;
    private int errorLines;

    /**
     * Add success.
     */
    public void addSuccess(){
        successLines++;
    }

    /**
     * Add error.
     */
    public void addError(){
        errorLines++;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public InsertRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(InsertRequestStatus status) {
        this.status = status;
    }

    /**
     * Gets start date.
     *
     * @return the start date 
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets start date.
     *
     * @param startDate the start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets summary.
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets summary.
     *
     * @param summary the result
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets success log path.
     *
     * @return the success log path
     */
    public String getSuccessLogPath() {
        return successLogPath;
    }

    /**
     * Sets success log path.
     *
     * @param successLogPath the success log path
     */
    public void setSuccessLogPath(String successLogPath) {
        this.successLogPath = successLogPath;
    }

    /**
     * Gets error log path.
     *
     * @return the error log path
     */
    public String getErrorLogPath() {
        return errorLogPath;
    }

    /**
     * Sets error log path.
     *
     * @param errorLogPath the error log path
     */
    public void setErrorLogPath(String errorLogPath) {
        this.errorLogPath = errorLogPath;
    }

    /**
     * Gets schedule date.
     *
     * @return the schedule date
     */
    public Date getScheduleDate() {
        return scheduleDate;
    }

    /**
     * Sets schedule date.
     *
     * @param scheduleDate the schedule date
     */
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    /**
     * Gets total files.
     *
     * @return the total files
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * Sets total files.
     *
     * @param totalLines the total files
     */
    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    /**
     * Gets success files.
     *
     * @return the success files
     */
    public int getSuccessLines() {
        return successLines;
    }

    /**
     * Sets success files.
     *
     * @param successLines the success files
     */
    public void setSuccessLines(int successLines) {
        this.successLines = successLines;
    }

    /**
     * Gets error lines.
     *
     * @return the error lines
     */
    public int getErrorLines() {
        return errorLines;
    }

    /**
     * Sets error lines.
     *
     * @param errorLines the error lines
     */
    public void setErrorLines(int errorLines) {
        this.errorLines = errorLines;
    }

    /**
     * Gets status code.
     *
     * @return the status code
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets status code.
     *
     * @param statusCode the status code
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets fileName.
     *
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets fileName.
     *
     * @param fileName the fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Id insert request.
     *
     * @param id the id
     * @return the insert request
     */
    public InsertRequest id(final Long id) {
        this.id = id;
        return this;
    }

    /**
     * Status insert request.
     *
     * @param status the status
     * @return the insert request
     */
    public InsertRequest status(final InsertRequestStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Start date insert request.
     *
     * @param startDate the start date
     * @return the insert request
     */
    public InsertRequest startDate(final Date startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * End date insert request.
     *
     * @param endDate the end date
     * @return the insert request
     */
    public InsertRequest endDate(final Date endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * Summary insert request.
     *
     * @param summary the result
     * @return the insert request
     */
    public InsertRequest summary(final String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * Success log path insert request.
     *
     * @param successLogPath the success log path
     * @return the insert request
     */
    public InsertRequest successLogPath(final String successLogPath) {
        this.successLogPath = successLogPath;
        return this;
    }

    /**
     * Error log path insert request.
     *
     * @param errorLogPath the error log path
     * @return the insert request
     */
    public InsertRequest errorLogPath(final String errorLogPath) {
        this.errorLogPath = errorLogPath;
        return this;
    }


    /**
     * Schedule date insert request.
     *
     * @param scheduleDate the schedule date
     * @return the insert request
     */
    public InsertRequest scheduleDate(final Date scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    /**
     * Total lines insert request.
     *
     * @param totalLines the total lines
     * @return the insert request
     */
    public InsertRequest totalLines(final int totalLines) {
        this.totalLines = totalLines;
        return this;
    }

    /**
     * Success lines insert request.
     *
     * @param successLines the success lines
     * @return the insert request
     */
    public InsertRequest successLines(final int successLines) {
        this.successLines = successLines;
        return this;
    }

    /**
     * Error lines insert request.
     *
     * @param errorLines the error lines
     * @return the insert request
     */
    public InsertRequest errorLines(final int errorLines) {
        this.errorLines = errorLines;
        return this;
    }

    /**
     * Status code insert request.
     *
     * @param statusCode the status code
     * @return the insert request
     */
    public InsertRequest statusCode(final String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * File insert request.
     *
     * @param fileName the fileName
     * @return the insert request
     */
    public InsertRequest fileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }


}
