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

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;

import java.util.List;

/**
 * Insert a request service
 *
 * @author alfonso.marin.lopez
 */
public interface InsertService {

    /**
     * Create or update the insert request and schedule the job
     *
     * @param insertRequest the insert request
     * @return insert request
     */
    InsertRequest saveInsertRequest(InsertRequest insertRequest);

    /**
     * Update the insert request information only and send ws notification
     *
     * @param insertRequest the insert request
     * @return insert request
     */
    InsertRequest updateInsertRequest(InsertRequest insertRequest);

    /**
     * Insert a request upload files in the system
     *
     * @param idInsertRequest the id insert request
     * @return insert request
     */
    InsertRequest startInsertProcess(Long idInsertRequest);

    /**
     * Pause insert process insert request.
     *
     * @param idInsertRequest the id insert request
     * @return the insert request
     */
    InsertRequest pauseInsertProcess(Long idInsertRequest);

    /**
     * Resume insert process insert request.
     *
     * @param idInsertRequest the id insert request
     * @return the insert request
     */
    InsertRequest resumeInsertProcess(Long idInsertRequest);

    /**
     * Abort insert process insert request.
     *
     * @param idInsertRequest the id insert request
     * @return the insert request
     */
    InsertRequest abortInsertProcess(Long idInsertRequest);

    /**
     * Find all insert request list.
     *
     * @return the list
     */
    List<InsertRequest> findAllInsertRequest();

    /**
     * Gets insert request.
     *
     * @param id the id
     * @return the insert request
     */
    InsertRequest getInsertRequest(Long id);

    /**
     * Change the status of insert request to delete if it's possible
     *
     * @param idInsertRequest the id insert request
     * @return insert request
     */
    InsertRequest deleteInsertRequest(Long idInsertRequest);
    
}
