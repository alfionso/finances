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

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.ApplicationConfig;
import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.exception.ApplicationException;
import es.alfonsomarin.finances.core.notification.NotificationService;
import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.insert.InsertService;
import es.alfonsomarin.finances.transaction.JobExecutionStubFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class DefaultBatchServiceTest {

    @Autowired
    ApplicationConfig applicationConfig;

    /* Mocks */
    @Mock
    private InsertService mockInsertService;
    @Mock
    private NotificationService mockNotificationService;
    @Mock
    private ConfigurationService mockConfigurationService;
    @Mock
    private JobExplorer mockJobExplorer;
    @Mock
    private JobLauncher mockJobLauncher;
    @Mock
    private InsertRequest mockInsertRequest;
    @Mock
    private Job mockJob;
    @Mock
    private JobOperator mockJobOperator;
    @Mock
    private JobExecution mockJobExecution;

    /* Private */
    private PathsUtils pathsUtils = new PathsUtils();
    private List<JobInstance> jobInstanceLst = JobExecutionStubFactory.generateJobInstanceList();
    private JobParameters jobParameters = JobExecutionStubFactory.generateJobParameters(TestConstants.JOB_INSTANCE_ID);
    private List<Long> executionsLst = new ArrayList<>(Arrays.asList(TestConstants.JOB_INSTANCE_ID));

    @InjectMocks
    private DefaultBatchService defaultBatchService;

    /**
     * The Expected exception.
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Setup.
     */
    @Before
    public void setup() {
        pathsUtils.setApplicationConfig(applicationConfig);
        
        defaultBatchService.setConfigurationService(mockConfigurationService);
        defaultBatchService.setInsertService(mockInsertService);
        defaultBatchService.setNotificationService(mockNotificationService);
        defaultBatchService.setJobExplorer(mockJobExplorer);
        defaultBatchService.setJobLauncher(mockJobLauncher);
        defaultBatchService.setInsertJob(mockJob);
        defaultBatchService.setJobOperator(mockJobOperator);
        defaultBatchService.setPathsUtils(pathsUtils);

        when(mockInsertRequest.getId()).thenReturn(1L);
        when(mockInsertRequest.getFileName()).thenReturn("filename");
        when(mockInsertService.saveInsertRequest(anyObject())).thenReturn(mockInsertRequest);
    }

    
    /**
     * Start Insert process with success result
     */
    @Test
    public void startProcessThenOk() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        defaultBatchService.startInsertOperations(mockInsertRequest);
        verify(mockJobLauncher, times(1)).run(anyObject(),anyObject());
    }

    /**
     * Start Insert process with exception result because the process exists
     */
    @Test(expected = ApplicationException.class)
    public void startProcessThenExceptionExists() throws NoSuchJobInstanceException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STARTED);
        defaultBatchService.startInsertOperations(mockInsertRequest);
    }

    /**
     * Start Insert process with exception in jobExecution 
     */
    @Test(expected = ApplicationException.class)
    public void startProcessThenExceptionInJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        doThrow(JobExecutionAlreadyRunningException.class).when(mockJobLauncher).run(anyObject(), anyObject());
        defaultBatchService.startInsertOperations(mockInsertRequest);
    }

    /**
     * Pause Insert process with success result
     */
    @Test
    public void pauseProcessThenOk() throws NoSuchJobInstanceException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STARTED);
        defaultBatchService.pauseInsertOperations(mockInsertRequest);
        verify(mockJobOperator, times(1)).stop(anyLong());
    }

    /**
     * Pause Insert process with Exception because job doesn't exist
     */
    @Test(expected = ApplicationException.class)
    public void pauseProcessThenExceptionNotFound() {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        defaultBatchService.pauseInsertOperations(mockInsertRequest);
    }

    /**
     * Pause Insert process with Exception stopping job
     */
    @Test(expected = ApplicationException.class)
    public void pauseProcessThenExceptionStoppingJob() throws NoSuchJobInstanceException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STARTED);
        doThrow(NoSuchJobExecutionException.class).when(mockJobOperator).stop(anyLong());
        defaultBatchService.pauseInsertOperations(mockInsertRequest);
    }

    /**
     * Resume Insert process with success result
     */
    @Test
    public void resumeProcessThenOk() throws NoSuchJobInstanceException, JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STOPPED);
        defaultBatchService.resumeInsertOperations(mockInsertRequest);
        verify(mockJobOperator, times(1)).restart(anyLong());
    }

    /**
     * Resume Insert process with exception not found job
     */
    @Test(expected = ApplicationException.class)
    public void resumeProcessThenExceptionNotFound() throws NoSuchJobInstanceException, JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        defaultBatchService.resumeInsertOperations(mockInsertRequest);
    }

    /**
     * Resume Insert process with exception restarting job
     */
    @Test(expected = ApplicationException.class)
    public void resumeProcessThenExceptionRestarting() throws NoSuchJobInstanceException, JobParametersInvalidException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STOPPED);
        doThrow(NoSuchJobException.class).when(mockJobOperator).restart(anyLong());
        defaultBatchService.resumeInsertOperations(mockInsertRequest);
    }

    /**
     * Abort Insert process with success result
     */
    @Test
    public void abortProcessThenOk() throws NoSuchJobInstanceException, NoSuchJobExecutionException, JobExecutionAlreadyRunningException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STOPPED);
        defaultBatchService.abortInsertOperations(mockInsertRequest);
        verify(mockJobOperator, times(1)).abandon(anyLong());
    }

    /**
     * Abort Insert process with exception not found
     */
    @Test(expected = ApplicationException.class)
    public void abortProcessThenExceptionNotFound() throws NoSuchJobInstanceException, NoSuchJobExecutionException, JobExecutionAlreadyRunningException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        defaultBatchService.abortInsertOperations(mockInsertRequest);
    }
    /**
     * Abort Insert process with success result
     */
    @Test(expected = ApplicationException.class)
    public void abortProcessThenExceptionAbandonJob() throws NoSuchJobInstanceException, NoSuchJobExecutionException, JobExecutionAlreadyRunningException {
        when(mockJobExplorer.findJobInstancesByJobName(anyString(), anyInt(), anyInt())).thenReturn(jobInstanceLst);
        when(mockJobOperator.getExecutions(anyLong())).thenReturn(executionsLst);
        when(mockJobExplorer.getJobExecution(anyLong())).thenReturn(mockJobExecution);
        when(mockJobExecution.getJobParameters()).thenReturn(jobParameters);
        when(mockJobExecution.getStatus()).thenReturn(BatchStatus.STOPPED);
        doThrow(NoSuchJobExecutionException.class).when(mockJobOperator).abandon(anyLong());
        defaultBatchService.abortInsertOperations(mockInsertRequest);
        
    }
}
