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

package es.alfonsomarin.finances.core.schedule;

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;

/**
 * Schedule service
 *
 * @author alfonso.marin.lopez
 */
public interface ScheduleService {

    /**
     * Schedule insert request.
     *
     * @param insertRequest the insert request
     */
    void scheduleInsertRequest(InsertRequest insertRequest);

    /**
     * Remove insert schedule.
     *
     * @param idInsertRequest the id insert request
     */
    void removeInsertSchedule(Long idInsertRequest);

    /**
     * Pause all.
     */
    void pauseAll();

    /**
     * Resume all.
     */
    void resumeAll();

    /**
     * Remove all.
     */
    void removeAll();
    
    
}
