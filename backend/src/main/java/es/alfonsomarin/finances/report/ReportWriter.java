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

import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;

/**
 * The interface Report writer.
 *
 * @author alfonso.marin.lopez
 */
public interface ReportWriter {
    /**
     * Error.
     *
     * @param message the message
     */
    void error(String message);

    /**
     * Error.
     *
     * @param metadataRecord the metadata record
     */
    void error(MetadataRecord metadataRecord);

    /**
     * Info.
     *
     * @param message the message
     */
    void info(String message);

    /**
     * Info.
     *
     * @param metadataRecord the metadata record
     */
    void info(MetadataRecord metadataRecord);
}
