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
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.core.exception.ApplicationException;
import es.alfonsomarin.finances.core.util.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultScheduleServiceTest {

    @InjectMocks
    private DefaultScheduleService scheduleService;
    @Mock
    private Scheduler mockScheduler;
    @Mock
    private SchedulerFactoryBean mockSchedulerFactoryBean;
    @Mock
    private Transaction mockTransaction;
    @Mock
    private InsertRequest mockInsertRequest;
    @Mock
    private JobDetail jobDetailMock;
    @Mock
    private Trigger triggerMock;

    /**
     * Setup.
     */
    @Before
    public void setup() throws SchedulerException {
        when(mockSchedulerFactoryBean.getScheduler()).thenReturn(mockScheduler);
        when(mockScheduler.getTriggersOfJob(anyObject())).thenReturn(new ArrayList<>());
        when(mockScheduler.unscheduleJob(anyObject())).thenReturn(true);
        when(jobDetailMock.getJobDataMap()).thenReturn(new JobDataMap());
        when(mockScheduler.getJobDetail(anyObject())).thenReturn(jobDetailMock);
        when(mockScheduler.getTrigger(anyObject())).thenReturn(triggerMock);
        when(mockScheduler.scheduleJob(jobDetailMock,triggerMock)).thenReturn(new Date());
        when(mockInsertRequest.getId()).thenReturn(1L);
        when(mockInsertRequest.getScheduleDate()).thenReturn(DateTimeUtils.nowPlus(5));
        scheduleService = new DefaultScheduleService(mockSchedulerFactoryBean);
    }

    /**
     * Schedule job return Insert request.
     *
     * @throws SchedulerException the mockScheduler exception
     */
    @Test
    public void scheduleJobReturnInsertRequest() throws SchedulerException {
        scheduleService.scheduleInsertRequest(mockInsertRequest);
        verify(mockScheduler, times(1)).scheduleJob(anyObject(),anyObject());
    }


    /**
     * Schedule job return exception.
     *
     * @throws SchedulerException the mockScheduler exception
     * @throws ParseException     the parse exception
     */
    @Test(expected = ApplicationException.class)
    public void scheduleJobReturnException() throws SchedulerException {
        when(mockScheduler.scheduleJob(anyObject(),anyObject())).thenThrow(new SchedulerException("msg"));
        scheduleService.scheduleInsertRequest(mockInsertRequest);

    }
}
