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

import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import es.alfonsomarin.finances.core.domain.notification.Notification;
import es.alfonsomarin.finances.core.domain.notification.NotificationType;
import es.alfonsomarin.finances.core.notification.NotificationService;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.core.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * The type Insert request validation end listener.
 *
 * @author alfonso.marin.lopez
 */
@Component
public class InsertRequestValidationEndListener extends JobExecutionListenerSupport{
    private static final Logger LOGGER = LogManager.getLogger(InsertRequestValidationEndListener.class);
    
    private PathsUtils pathsUtils;
    
    private InsertService insertService;
    
    private NotificationService notificationService;
    
    private ConfigurationService configurationService;

    private static int SUBSTRING_END = 63;

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        Long idRequest = jobExecution.getJobParameters().getLong(Constants.JOB_PARAM_ID_INSERT_REQUEST);
        Assert.isTrue(!idRequest.equals(0L), "Job insert execution without insert request ID");
        
        InsertRequest insertRequest = insertService.getInsertRequest(idRequest);
        Assert.notNull(insertRequest, "Insert request not found with ID["+idRequest+"]");
        if(insertRequest.getSuccessLines()>0) {
            insertRequest.setSuccessLogPath(
                    this.pathsUtils.relativePathInsertSuccess(insertRequest.getId()).toString());
        }
        if(insertRequest.getErrorLines()>0){
            insertRequest.setErrorLogPath(
                    this.pathsUtils.relativePathInsertError(insertRequest.getId()).toString());
        }
        insertRequest.setStatusCode(null);
        switch (jobExecution.getStatus()){
            case STOPPED:
                insertRequest.setStatus(InsertRequestStatus.PAUSED);
                insertRequest.setEndDate(null);
                break;
            case COMPLETED:
                if(insertRequest.getErrorLines()>0){
                    insertRequest.setStatus(InsertRequestStatus.FAILED);
                }else{
                    insertRequest.setStatus(InsertRequestStatus.SUCCESS);
                }
                insertRequest.setEndDate(new Date());
                break;
            case FAILED: //fail not related with the process(not metadatafile, connection error, etc ..)
                insertRequest.setStatus(InsertRequestStatus.FAILED);
                insertRequest.statusCode(StringUtils.subString(
                        jobExecution.getExitStatus().getExitDescription(),
                        0,
                        SUBSTRING_END));
                insertRequest.setEndDate(null);
                break;
            default:
                insertRequest.setStatus(InsertRequestStatus.FAILED);
                insertRequest.statusCode("Job execution finish with unexpected status ["+jobExecution.getStatus().toString()+"]");
                insertRequest.setEndDate(new Date());
                break;
        }

        insertService.updateInsertRequest(insertRequest);
        if(jobExecution.getStatus().equals(BatchStatus.COMPLETED) ||
                jobExecution.getStatus().equals(BatchStatus.FAILED)){
            sendNotification(insertRequest);   
        }

        LOGGER.info("Job finished status:"+jobExecution.getStatus());
        LOGGER.info("Job finished transaction status:"+insertRequest.getStatus());
    }

    private void sendNotification(InsertRequest insertRequest){
        Notification notification = new Notification();
        notification.setData(insertRequest);
        notification.setEntityName(DefaultInsertService.KEY_INSERT);
        notification.setRecipients(configurationService.getListMailsRecipients());
        notification.setType(NotificationType.INSERT_FINISHED);
        notificationService.sendNotification(notification);
    }

    /**
     * Sets paths utils.
     *
     * @param pathsUtils the paths utils
     */
    @Autowired
    public void setPathsUtils(PathsUtils pathsUtils) {
        this.pathsUtils = pathsUtils;
    }

    /**
     * Sets insert service.
     *
     * @param insertService the insert service
     */
    @Autowired
    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    /**
     * Sets notification service.
     *
     * @param notificationService the notification service
     */
    @Autowired
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Sets configuration service.
     *
     * @param configurationService the configuration service
     */
    @Autowired
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
