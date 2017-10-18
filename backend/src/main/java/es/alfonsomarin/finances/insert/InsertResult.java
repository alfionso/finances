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

import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;

/**
 * The type Insert result.
 *
 * @author alfonso.marin.lopez
 */
public class InsertResult {
    
    private boolean inserted;
    
    private String errorMessage;
    
    private MetadataRecord metadataRecord;

    /**
     * Is inserted boolean.
     *
     * @return the boolean
     */
    public boolean isInserted() {
        return inserted;
    }

    /**
     * Sets inserted.
     *
     * @param inserted the inserted
     */
    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Gets metadata record.
     *
     * @return the metadata record
     */
    public MetadataRecord getMetadataRecord() {
        return metadataRecord;
    }

    /**
     * Sets metadata record.
     *
     * @param metadataRecord the metadata record
     */
    public void setMetadataRecord(MetadataRecord metadataRecord) {
        this.metadataRecord = metadataRecord;
    }

    /**
     * inserted result.
     *
     * @param inserted the inserted
     * @return the insert result
     */
    public InsertResult inserted(final boolean inserted) {
        this.inserted = inserted;
        return this;
    }

    /**
     * Error message insert result.
     *
     * @param errorMessage the error message
     * @return the insert result
     */
    public InsertResult errorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Metadata record insert result.
     *
     * @param metadataRecord the metadata record
     * @return the insert result
     */
    public InsertResult metadataRecord(final MetadataRecord metadataRecord) {
        this.metadataRecord = metadataRecord;
        return this;
    }


}
