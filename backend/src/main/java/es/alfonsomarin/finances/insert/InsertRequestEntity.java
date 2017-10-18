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

package es.alfonsomarin.finances.insert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Insert request entity
 *
 * @author alfonso.marin.lopez
 */
@Entity
//@Inheritance(strategy= InheritanceType.JOINED)
public class InsertRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    
    @Column
    private Date scheduleDate;
    
    @Column
    private String status;
    
    @Column
    private String statusCode;
    
    @Column
    private Date startDate;
    
    @Column
    private Date endDate;

    @Column
    private String fileName;
    
    @Column
    private String summary;

    @Column
    private String successLogPath;

    @Column
    private String errorLogPath;
    
    @Column
    private int totalLines;
    
    @Column
    private int successLines;
    
    @Column
    private int errorLines;

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
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
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
     * Gets file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets file name.
     *
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
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
     * Gets error files.
     *
     * @return the error files
     */
    public int getErrorLines() {
        return errorLines;
    }

    /**
     * Sets error files.
     *
     * @param errorLines the error files
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
     * Id insert request entity.
     *
     * @param id the id
     * @return the insert request entity
     */
    public InsertRequestEntity id(final Long id) {
        this.id = id;
        return this;
    }

    /**
     * Schedule date insert request entity.
     *
     * @param scheduleDate the schedule date
     * @return the insert request entity
     */
    public InsertRequestEntity scheduleDate(final Date scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    /**
     * Status insert request entity.
     *
     * @param status the status
     * @return the insert request entity
     */
    public InsertRequestEntity status(final String status) {
        this.status = status;
        return this;
    }

    /**
     * Start date insert request entity.
     *
     * @param startDate the start date
     * @return the insert request entity
     */
    public InsertRequestEntity startDate(final Date startDate) {
        this.startDate = startDate;
        return this;
    }

    /**
     * End date insert request entity.
     *
     * @param endDate the end date
     * @return the insert request entity
     */
    public InsertRequestEntity endDate(final Date endDate) {
        this.endDate = endDate;
        return this;
    }

    /**
     * File name insert request entity.
     *
     * @param fileName the file name
     * @return the insert request entity
     */
    public InsertRequestEntity fileName(final String fileName) {
        this.fileName = fileName;
        return this;
    }


    /**
     * Summary insert request entity.
     *
     * @param summary the summary
     * @return the insert request entity
     */
    public InsertRequestEntity summary(final String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * Success log path insert request entity.
     *
     * @param successLogPath the success log path
     * @return the insert request entity
     */
    public InsertRequestEntity successLogPath(final String successLogPath) {
        this.successLogPath = successLogPath;
        return this;
    }

    /**
     * Error log path insert request entity.
     *
     * @param errorLogPath the error log path
     * @return the insert request entity
     */
    public InsertRequestEntity errorLogPath(final String errorLogPath) {
        this.errorLogPath = errorLogPath;
        return this;
    }


    /**
     * Total files insert request entity.
     *
     * @param totalLines the total lines
     * @return the insert request entity
     */
    public InsertRequestEntity totalLines(final int totalLines) {
        this.totalLines = totalLines;
        return this;
    }

    /**
     * Success files insert request entity.
     *
     * @param successLines the success lines
     * @return the insert request entity
     */
    public InsertRequestEntity successLines(final int successLines) {
        this.successLines = successLines;
        return this;
    }

    /**
     * Error files insert request entity.
     *
     * @param errorLines the error lines
     * @return the insert request entity
     */
    public InsertRequestEntity errorLines(final int errorLines) {
        this.errorLines = errorLines;
        return this;
    }

    /**
     * Status code insert request entity.
     *
     * @param statusCode the status code
     * @return the insert request entity
     */
    public InsertRequestEntity statusCode(final String statusCode) {
        this.statusCode = statusCode;
        return this;
    }


}
