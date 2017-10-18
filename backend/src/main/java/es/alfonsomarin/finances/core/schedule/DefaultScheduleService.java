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

import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.insert.DefaultInsertService;
import es.alfonsomarin.finances.insert.InsertScheduleJob;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Default schedule service
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultScheduleService implements ScheduleService{
    private static final Logger LOGGER = LogManager.getLogger(DefaultInsertService.class);
    private final Scheduler scheduler;

    /**
     * Instantiates a new Default schedule service.
     *
     * @param scheduler the scheduler
     */
    @Autowired
    public DefaultScheduleService(SchedulerFactoryBean scheduler){
        this.scheduler = scheduler.getScheduler();
    }
    
    @Override
    public void scheduleInsertRequest(InsertRequest insertRequest) {
        Assert.notNull(insertRequest.getId(),"Insert request ID cannot be null");
        Assert.notNull(insertRequest.getScheduleDate(), "Insert request Schedule date cannot be null");
        try{
            JobKey jobKey = getJobKey(insertRequest.getId(), Constants.SKD_GROUP_INSERT);
            TriggerKey triggerKey = getTriggerKey(insertRequest.getId(), Constants.SKD_TRIGGER_GROUP_INSERT);
            unscheduleJob(jobKey);

            JobDetail jobDetail = Optional.ofNullable(scheduler.getJobDetail(jobKey))
                    .orElse(JobBuilder.newJob(InsertScheduleJob.class).withIdentity(jobKey).build());
            jobDetail.getJobDataMap().put(Constants.SKD_PARAM_ID_INSERT,insertRequest.getId());

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(dateToCron(insertRequest.getScheduleDate()));

            Trigger trigger = Optional.ofNullable(scheduler.getTrigger(triggerKey))
                    .orElse(TriggerBuilder.newTrigger()
                            .withIdentity(triggerKey)
                            .startNow()
                            .withSchedule(cronScheduleBuilder)
                            .build());

            scheduler.scheduleJob(jobDetail, trigger);
        }catch (SchedulerException e){
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .exceptionType(ExceptionType.DATA_INTEGRITY)
                    .code(MessageCodes.CODE_SCHEDULE_SCHEDULING)
                    .resources("Insert request", insertRequest.getId())
                    .build();
        }
    }
    
    @Override
    public void removeInsertSchedule(Long idInsertRequest) {
        Assert.notNull(idInsertRequest,"Insert request ID cannot be null");
        JobKey jobKey = getJobKey(idInsertRequest, Constants.SKD_GROUP_INSERT);
        unscheduleJob(jobKey);
    }

    @Autowired
    public void pauseAll(){
        try {
            this.scheduler.pauseAll();
        } catch (SchedulerException e) {
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .code(MessageCodes.CODE_UNEXPECTED_ERROR)
                    .exceptionType(ExceptionType.UNEXPECTED)
                    .build();
        }
    }
    
    @Autowired
    public void resumeAll() {
        try {
            this.scheduler.resumeAll();
        } catch (SchedulerException e) {
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .code(MessageCodes.CODE_UNEXPECTED_ERROR)
                    .exceptionType(ExceptionType.UNEXPECTED)
                    .build();
        }
    }
    
    @Autowired
    public void removeAll(){
        try {
            LOGGER.info("====== UNSCHEDULING ");

            for(String triggerGroupName : this.scheduler.getTriggerGroupNames()){
                for(TriggerKey triggerKey : this.scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName))){
                    this.scheduler.unscheduleJob(triggerKey);
                }
            }
        }catch (SchedulerException e){
            LOGGER.error(e);
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .code(MessageCodes.CODE_UNEXPECTED_ERROR)
                    .exceptionType(ExceptionType.UNEXPECTED)
                    .build();
        }
    }
    
    private String dateToCron(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND)+" "+
                calendar.get(Calendar.MINUTE)+" "+
                calendar.get(Calendar.HOUR_OF_DAY)+" "+
                calendar.get(Calendar.DAY_OF_MONTH)+" "+
                (calendar.get(Calendar.MONTH)+1)+" "+
                "? "+
                calendar.get(Calendar.YEAR);        
    }
    
    private JobKey getJobKey(Long idCollect, String group){
        return JobKey.jobKey(idCollect.toString(),group);
    }
    private TriggerKey getTriggerKey(Long idCollect, String group){
        return TriggerKey.triggerKey(idCollect.toString(),group);
    }
    
    private void unscheduleJob(JobKey jobKey){
        try{
            List<? extends Trigger> existingTriggers = this.scheduler.getTriggersOfJob(jobKey);
            for (Trigger existingTrigger : existingTriggers) {
                scheduler.unscheduleJob(existingTrigger.getKey());
            }
        }catch(SchedulerException e){
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .exceptionType(ExceptionType.RESOURCE_NOT_FOUND)
                    .code(MessageCodes.CODE_SCHEDULE_UNSCHEDULE)
                    .resources(jobKey.getName(),jobKey.getGroup())
                    .build();
        }
        
    }
}
