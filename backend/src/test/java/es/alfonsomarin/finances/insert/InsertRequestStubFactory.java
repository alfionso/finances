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
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import es.alfonsomarin.finances.core.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfonso.marin.lopez
 */
public class InsertRequestStubFactory {
    /**
     * Generate transaction entity.
     *
     * @return the transaction entity
     */
    public static InsertRequestEntity generateInsertRequestEntity(){
        return new InsertRequestEntity()
                .id(1L)
                .status(InsertRequestStatus.NOT_STARTED.name())
                .fileName("filename")
                .scheduleDate(DateTimeUtils.now())
                .totalLines(10);
    }

    /**
     * Generate transaction.
     *
     * @return the transaction
     */
    public static InsertRequest generateInsertRequest(){
        return new InsertRequest()
                .id(1L)
                .status(InsertRequestStatus.NOT_STARTED)
                .fileName("filename")
                .scheduleDate(DateTimeUtils.now())
                .successLines(10);
    }

   

    /**
     * Generate transaction request transaction request list.
     *
     * @return the list
     */
    public static List<InsertRequest> generateInsertRequestList() {
        List<InsertRequest> retList = new ArrayList<>();
        retList.add(generateInsertRequest());
        return retList;
    }

    /**
     * Generate transaction request transaction request entity list.
     *
     * @return the list
     */
    public static List<InsertRequestEntity> generateInsertRequestEntityList() {
        List<InsertRequestEntity> retList = new ArrayList<>();
        retList.add(generateInsertRequestEntity());
        return retList;
    }
}
