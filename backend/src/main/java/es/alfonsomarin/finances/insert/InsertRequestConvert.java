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

import es.alfonsomarin.finances.transaction.TransactionConvert;
import es.alfonsomarin.finances.core.domain.common.converter.Converter;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

/**
 * The type Insert request convert.
 *
 * @author alfonso.marin.lopez
 */
@Component
public class InsertRequestConvert implements Converter<InsertRequest, InsertRequestEntity> {

    private TransactionConvert collectRequestConvert;
    
    @Override
    public InsertRequestEntity toPersistence(InsertRequest collectRequest) {
        return ofNullable(collectRequest)
                .map(
                        p -> toPersistence(p, new InsertRequestEntity())
                )
                .orElse(null);
    }

    @Override
    public InsertRequestEntity toPersistence(InsertRequest insertRequest, InsertRequestEntity insertRequestEntity) {
        return insertRequestEntity.id(insertRequest.getId())
                .scheduleDate(insertRequest.getScheduleDate())
                .status(insertRequest.getStatus().toString())
                .statusCode(insertRequest.getStatusCode())
                .startDate(insertRequest.getStartDate())
                .endDate(insertRequest.getEndDate())
                .summary(insertRequest.getSummary())
                .errorLogPath(insertRequest.getErrorLogPath())
                .successLogPath(insertRequest.getSuccessLogPath())
                .totalLines(insertRequest.getTotalLines())
                .successLines(insertRequest.getSuccessLines())
                .errorLines(insertRequest.getErrorLines())
                .fileName(insertRequest.getFileName());
    }

    @Override
    public InsertRequest toCore(InsertRequestEntity insertRequestEntity) {
        return ofNullable(insertRequestEntity)
                .map(p -> {
                            InsertRequest insertRequest = new InsertRequest()
                                    .id(insertRequestEntity.getId())
                                    .scheduleDate(insertRequestEntity.getScheduleDate())
                                    .status(InsertRequestStatus.valueOf(insertRequestEntity.getStatus()))
                                    .statusCode(insertRequestEntity.getStatusCode())
                                    .startDate(insertRequestEntity.getStartDate())
                                    .endDate(insertRequestEntity.getEndDate())
                                    .summary(insertRequestEntity.getSummary())
                                    .errorLogPath(insertRequestEntity.getErrorLogPath())
                                    .successLogPath(insertRequestEntity.getSuccessLogPath())
                                    .totalLines(insertRequestEntity.getTotalLines())
                                    .successLines(insertRequestEntity.getSuccessLines())
                                    .errorLines(insertRequestEntity.getErrorLines())
                                    .fileName(insertRequestEntity.getFileName());
                            return insertRequest;
                        }
                )
                .orElse(null);
    }

    /**
     * Sets transaction request convert.
     *
     * @param collectRequestConvert the transaction request convert
     */
    @Autowired
    public void setCollectRequestConvert(TransactionConvert collectRequestConvert) {
        this.collectRequestConvert = collectRequestConvert;
    }
}
