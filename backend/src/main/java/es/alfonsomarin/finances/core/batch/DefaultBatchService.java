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
package es.alfonsomarin.finances.core.batch;

import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import es.alfonsomarin.finances.core.notification.NotificationService;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.insert.InsertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Default batch service
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultBatchService implements BatchService {
    
    private static final Logger LOGGER = LogManager.getLogger(DefaultBatchService.class);
        
    private JobLauncher jobLauncher;
    
    private JobOperator jobOperator;
    
    private JobExplorer jobExplorer;
    
    private Job insertJob;
    
    private PathsUtils pathsUtils;

    private NotificationService notificationService;

    private ConfigurationService configurationService;
    
    private InsertService insertService;
    
    public static final int NUMBER_INSTANCE_JOB = 10;


    @Override
    public void startInsertOperations(InsertRequest insertRequest) {
        Optional<List<JobExecution>> executions = getJobExecution(insertRequest, BatchStatus.STARTED);
        if(!executions.isPresent()){
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(Constants.JOB_PARAM_PATH_FILE,
                            pathsUtils.absolutePathInsert(insertRequest.getId(),insertRequest.getFileName())
                                    .toString())
                    .addLong(Constants.JOB_PARAM_ID_INSERT_REQUEST, insertRequest.getId())
                    .addLong("time", new Date().getTime()) //mandatory to re-run the same instance
                    .toJobParameters();
            try {
                jobLauncher.run(insertJob,jobParameters);
                LOGGER.debug("Insertion  with ID [{}] started", insertRequest.getId());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
                throw new ExceptionBuilder()
                        .message("Unexpected batch error.")
                        .exceptionType(ExceptionType.BATCH_EJECUTION)
                        .exception(e)
                        .code(MessageCodes.CODE_BATCH_INTERNAL_ERROR)
                        .resources("Insert", insertRequest.getId())
                        .build();
            }
        }else{
            throw new ExceptionBuilder()
                    .message("Error starting job.")
                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                    .code(MessageCodes.CODE_BATCH_ALREADY_RUNNING_JOB)
                    .resources(insertRequest.getId())
                    .build(); 
        }
    }

    @Override
    public void pauseInsertOperations(InsertRequest insertRequest) {
        Optional<List<JobExecution>> executions = getJobExecution(insertRequest, BatchStatus.STARTED);
        if(executions.isPresent()){
            executions.get()
                    .stream()
                    .findFirst()
                    .ifPresent(lastExecution -> {
                        try {
                            jobOperator.stop(lastExecution.getId());
                        } catch (NoSuchJobExecutionException | JobExecutionNotRunningException e) {
                            throw new ExceptionBuilder()
                                    .message("Unexpected batch error.")
                                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                                    .exception(e)
                                    .code(MessageCodes.CODE_BATCH_INTERNAL_ERROR)
                                    .resources("Insert", insertRequest.getId())
                                    .build();
                        }
                    });
        }else{
            throw new ExceptionBuilder()
                    .message("Error pausing the job.")
                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                    .code(MessageCodes.CODE_BATCH_NOT_FOUND)
                    .resources(insertRequest.getId(),BatchStatus.STARTED.name())
                    .build();
        }
    }

    @Override
    public void resumeInsertOperations(InsertRequest insertRequest) {
        Optional<List<JobExecution>> executions =  getJobExecution(insertRequest, BatchStatus.STOPPED);
        if(executions.isPresent()){
            executions.get().stream()
                    .findFirst()
                    .ifPresent(lastExecution ->{
                        try {
                            jobOperator.restart(lastExecution.getId());
                        } catch (NoSuchJobException|JobInstanceAlreadyCompleteException|JobRestartException|
                                NoSuchJobExecutionException|JobParametersInvalidException e) {
                            throw new ExceptionBuilder()
                                    .message("Unexpected batch error.")
                                    .exception(e)
                                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                                    .code(MessageCodes.CODE_BATCH_INTERNAL_ERROR)
                                    .resources("Insert", insertRequest.getId())
                                    .build();
                        } 
                    });
        }else{
            throw new ExceptionBuilder()
                    .message("Error resuming the job.")
                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                    .code(MessageCodes.CODE_BATCH_NOT_FOUND)
                    .resources(insertRequest.getId(),BatchStatus.STOPPED.name())
                    .build();
        }
    }

    @Override
    public void abortInsertOperations(InsertRequest insertRequest) {
        Optional<List<JobExecution>> executions = getJobExecution(insertRequest,BatchStatus.STOPPED);
        if(executions.isPresent()){
            executions.get().forEach(jobExecution -> {
                try {
                    jobOperator.abandon(jobExecution.getId());
                } catch (NoSuchJobExecutionException | JobExecutionAlreadyRunningException e) {
                    throw new ExceptionBuilder()
                            .message("Unexpected batch error.")
                            .exceptionType(ExceptionType.BATCH_EJECUTION)
                            .exception(e)
                            .code(MessageCodes.CODE_BATCH_INTERNAL_ERROR)
                            .resources("Insert", insertRequest.getId())
                            .build();
                }
            });
        }else{
            throw new ExceptionBuilder()
                    .message("Error aborting the job.")
                    .exceptionType(ExceptionType.BATCH_EJECUTION)
                    .code(MessageCodes.CODE_BATCH_NOT_FOUND)
                    .resources(insertRequest.getId(),BatchStatus.STOPPED.name())
                    .build();
            
        }
    }

    
    /**
     * Set job explorer.
     *
     * @param jobExplorer the job explorer
     */
    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer){
        this.jobExplorer = jobExplorer;
    }
    
    private Optional<List<JobExecution>> getJobExecution(InsertRequest insertRequest, BatchStatus status){
        List<JobExecution> listResult = new ArrayList<>();

        List<JobInstance> instances = jobExplorer.findJobInstancesByJobName(insertJob.getName(),0,NUMBER_INSTANCE_JOB);

        Optional.of(instances).ifPresent(listInstances -> listInstances.stream().forEach(instance -> {

                List<Long> jobExecutions = null;
                try {
                    jobExecutions = jobOperator.getExecutions(instance.getInstanceId());
                } catch (NoSuchJobInstanceException e) {
                    LOGGER.error(e.getMessage());
                }
                jobExecutions.stream().findFirst().ifPresent(id -> {
                    JobExecution jobExecution = jobExplorer.getJobExecution(id);
                    if(jobExecution.getJobParameters().getString(Constants.JOB_PARAM_ID_INSERT_REQUEST)
                            .equals(insertRequest.getId().toString()) &&
                            jobExecution.getStatus().equals(status)){
                        listResult.add(jobExecution);
                    }
                });
            }));
        if(listResult.isEmpty()) {
            return Optional.ofNullable(null);
        }else{
            return Optional.of(listResult);
        }
    }
    
    /**
     * Sets job launcher.
     *
     * @param jobLauncher the job launcher
     */
    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    /**
     * Set paths utils.
     *
     * @param pathsUtils the paths utils
     */
    @Autowired
    public void setPathsUtils(PathsUtils pathsUtils){
        this.pathsUtils = pathsUtils;
    }

    /**
     * Set insert job.
     *
     * @param insertJob the insert job
     */
    @Autowired
    @Qualifier("insertJob")
    public void setInsertJob(Job insertJob){
        this.insertJob = insertJob;
    }

    /**
     * Set job operator.
     *
     * @param jobOperator the job operator
     */
    @Autowired
    public void setJobOperator(JobOperator jobOperator){
        this.jobOperator = jobOperator;
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
    

    /**
     * Sets insert service.
     *
     * @param insertService the insert service
     */
    @Autowired
    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }
    
}
