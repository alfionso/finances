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

package es.alfonsomarin.finances.core.domain.metadata;

import org.springframework.util.StringUtils;

/**
 * The type Metadata record.
 *
 * @author alfonso.marin.lopez
 */
public class MetadataRecord {

    private String date;
    private String description;
    private String value;
    private String error;
    private String pathFile;

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets error.
     *
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Sets error.
     *
     * @param error the error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Add error.
     *
     * @param error the error
     */
    public void addError(String error){
        if(StringUtils.hasText(error)){
            if(StringUtils.hasText(this.error)){
                this.error += "/ ";
            }
            this.error += error;
        }
    }

    /**
     * Gets path file.
     *
     * @return the path file
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     * Sets path file.
     *
     * @param pathFile the path file
     */
    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    /**
     * Date record.
     *
     * @param date the date
     * @return the metadata record
     */
    public MetadataRecord date(final String date) {
        this.date = date;
        return this;
    }

    /**
     * Error metadata record.
     *
     * @param error the error
     * @return the metadata record
     */
    public MetadataRecord error(final String error) {
        this.error = error;
        return this;
    }

    /**
     * Path metadata file metadata record.
     *
     * @param pathMetadataFile the path metadata file
     * @return the metadata record
     */
    public MetadataRecord pathMetadataFile(final String pathMetadataFile) {
        this.pathFile = pathMetadataFile;
        return this;
    }

    /**
     * Description metadata record.
     *
     * @param description the description
     * @return the metadata record
     */
    public MetadataRecord description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Value metadata record.
     *
     * @param value the value
     * @return the metadata record
     */
    public MetadataRecord value(final String value) {
        this.value = value;
        return this;
    }


}
