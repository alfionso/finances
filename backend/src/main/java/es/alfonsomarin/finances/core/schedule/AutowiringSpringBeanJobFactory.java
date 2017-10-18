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
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Adds autowiring support to quartz jobs.
 * Created by david on 2015-01-20.
 *
 * @author alfonso.marin.lopez
 * @see https://gist.github.com/jelies/5085593
 */
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
        ApplicationContextAware {

    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle)  {
        try {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }catch(Exception e){
            throw new ExceptionBuilder()
                    .exception(e)
                    .message(e.getMessage())
                    .exceptionType(ExceptionType.UNEXPECTED)
                    .code(MessageCodes.CODE_UNEXPECTED_ERROR)
                    .build();
        }
    }
}
