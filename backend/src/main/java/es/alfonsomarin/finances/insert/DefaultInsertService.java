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

import es.alfonsomarin.finances.core.batch.BatchService;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import es.alfonsomarin.finances.core.schedule.ScheduleService;
import es.alfonsomarin.finances.core.util.DateTimeUtils;
import es.alfonsomarin.finances.core.wscommunication.WSCommunicationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Default insert service.
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultInsertService implements InsertService{

    private static final Logger LOGGER = LogManager.getLogger(DefaultInsertService.class);

    /**
     * The constant KEY_INSERT.
     */
    public static final String KEY_INSERT = "insert";
    
    private InsertRequestConvert insertRequestConvert;
    
    private InsertRequestRepository insertRequestRepository;
    
    private BatchService batchService;
    
    private WSCommunicationService wsCommunicationService;
    
    private ScheduleService scheduleService;
    
    private static final List<InsertRequestStatus> statusModifiedAllowed = Arrays.asList(
            InsertRequestStatus.NOT_STARTED, 
            InsertRequestStatus.PAUSED);

    @Override
    public InsertRequest saveInsertRequest(InsertRequest insertRequest) {
        
        LOGGER.debug("Creation of a insert request");
        if(statusModifiedAllowed.contains(insertRequest.getStatus())){
            
            //the schedule date has to be after now and insert will be schedule
            insertRequest.setStatus(InsertRequestStatus.NOT_STARTED);

            if(insertRequest.getScheduleDate()!=null &&
                    insertRequest.getScheduleDate().before(DateTimeUtils.now())) {
                throw new ExceptionBuilder()
                        .message("The schedule date has to be after now")
                        .exceptionType(ExceptionType.DATA_INTEGRITY)
                        .code(MessageCodes.CODE_SCHEDULE_SCHEDULING)
                        .resources("Insert request", insertRequest.getId())
                        .build();
            }
            if(existInsertRequest(insertRequest)){
                insertRequest = updateInsertRequest(insertRequest);
            }else{
                insertRequest = insertRequestConvert.toCore(
                        insertRequestRepository.save(
                                insertRequestConvert.toPersistence(insertRequest)));
                wsCommunicationService.sendToAllInsert(insertRequest);
            }

            if(insertRequest.getScheduleDate()!=null) {
                scheduleService.scheduleInsertRequest(insertRequest);
            }

        }else{
            throw new ExceptionBuilder()
                    .message("Error saving insert request.")
                    .exceptionType(ExceptionType.DATA_INTEGRITY)
                    .code(MessageCodes.CODE_NOT_ALLOWED_UPDATE_INSERT)
                    .resources(insertRequest.getId(),
                            statusModifiedAllowed.stream()
                                    .map(status -> status.name())
                                    .collect(Collectors.joining(",")))
                    .build();
        }
        return insertRequest;
    }

    @Override
    public InsertRequest updateInsertRequest(InsertRequest insertRequest) {
        insertRequest = insertRequestConvert.toCore(
                insertRequestRepository.save(
                        insertRequestConvert.toPersistence(insertRequest, 
                                getInsertRequestEntity(insertRequest.getId()))));
        wsCommunicationService.sendToAllInsert(insertRequest);
        return insertRequest;
    }

    @Override
    public List<InsertRequest> findAllInsertRequest() {
        return insertRequestConvert.toCore(insertRequestRepository.findAll());
    }

    @Override
    public InsertRequest getInsertRequest(Long id) {
        return insertRequestConvert.toCore(insertRequestRepository.findOne(id));
    }


    @Override
    public InsertRequest deleteInsertRequest(Long idInsertRequest) {
        InsertRequest insertRequest = getInsertRequest(idInsertRequest);
        if(insertRequest.getStatus().equals(InsertRequestStatus.NOT_STARTED)){
            scheduleService.removeInsertSchedule(idInsertRequest);
            updateInsertRequest(insertRequest.status(InsertRequestStatus.DELETED));
        }else{
            throw new ExceptionBuilder()
                    .message("Error on deleting a insert request.")
                    .exceptionType(ExceptionType.DATA_INTEGRITY)
                    .code(MessageCodes.CODE_NOT_ALLOWED_DELETE_INSERT)
                    .resources(insertRequest.getId())
                    .build();
        }

        return insertRequest;
    }

    @Override
    public InsertRequest startInsertProcess(Long idInsertRequest) {
        LOGGER.info("Insertion process started ");
        InsertRequest insertRequest = getInsertRequest(idInsertRequest);
        InsertRequestStatus currentStatus = insertRequest.getStatus();
        if(statusModifiedAllowed.contains(insertRequest.getStatus())){
            scheduleService.removeInsertSchedule(idInsertRequest);
            if(currentStatus.equals(InsertRequestStatus.NOT_STARTED)){
                updateInsertRequest(insertRequest
                        .successLines(0)
                        .errorLines(0)
                        .status(InsertRequestStatus.IN_PROGRESS)
                        .statusCode(null)
                        .startDate(new Date())
                        .endDate(null));
                batchService.startInsertOperations(insertRequest);
            }else{
                this.resumeInsertProcess(idInsertRequest);
            }
        }else{
            throw new ExceptionBuilder()
                    .message("Not possible to start insertion process.")
                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                    .code(MessageCodes.CODE_NOT_ALLOWED_START_INSERTION)
                    .resources(idInsertRequest, InsertRequestStatus.NOT_STARTED + "," + InsertRequestStatus.PAUSED)
                    .build();
        }
        return insertRequest;
    }

    @Override
    public InsertRequest pauseInsertProcess(Long idInsertRequest) {
        InsertRequest insertRequest = getInsertRequest(idInsertRequest);
        batchService.pauseInsertOperations(insertRequest);
        return insertRequest;
    }

    @Override
    public InsertRequest resumeInsertProcess(Long idInsertRequest) {
        InsertRequest insertRequest = getInsertRequest(idInsertRequest);
        insertRequest = updateInsertRequest(insertRequest
                .status(InsertRequestStatus.IN_PROGRESS)
                .statusCode(null)
                .endDate(null));
        batchService.resumeInsertOperations(insertRequest);
        return insertRequest;
    }

    @Override
    public InsertRequest abortInsertProcess(Long idInsertRequest) {
        InsertRequest insertRequest = getInsertRequest(idInsertRequest);
        batchService.abortInsertOperations(insertRequest);
        return updateInsertRequest(insertRequest
                .status(InsertRequestStatus.ABORTED)
                .statusCode(null)
                .endDate(new Date()));
    }

    /**
     * Sets insert request convert.
     *
     * @param insertRequestConvert the insert request convert
     */
    @Autowired
    public void setInsertRequestConvert(InsertRequestConvert insertRequestConvert) {
        this.insertRequestConvert = insertRequestConvert;
    }

    /**
     * Sets insert request repository.
     *
     * @param insertRequestRepository the insert request repository
     */
    @Autowired
    public void setInsertRequestRepository(InsertRequestRepository insertRequestRepository) {
        this.insertRequestRepository = insertRequestRepository;
    }
    
    /**
     * Sets batch service.
     *
     * @param batchService the batch service
     */
    @Autowired
    public void setBatchService(BatchService batchService) {
        this.batchService = batchService;
    }

    /**
     * Sets ws communication service.
     *
     * @param wsCommunicationService the ws communication service
     */
    @Autowired
    public void setWsCommunicationService(WSCommunicationService wsCommunicationService) {
        this.wsCommunicationService = wsCommunicationService;
    }

    /**
     * Sets schedule service.
     *
     * @param scheduleService the schedule service
     */
    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    private boolean existInsertRequest(InsertRequest insertRequest){
        return insertRequest.getId() != null && insertRequestRepository.findOne(insertRequest.getId()) != null;
    }
    private InsertRequestEntity getInsertRequestEntity(Long id){
        InsertRequestEntity entity = insertRequestRepository.findOne(id);
        if(entity==null){
            throw new ExceptionBuilder()
                    .message("Insert request not exists.")
                    .exceptionType(ExceptionType.DATA_INTEGRITY)
                    .code(MessageCodes.CODE_NOT_FOUND_INSERT)
                    .resources(id.toString())
                    .build();
        }
        return entity;

    }
}
