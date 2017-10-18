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

package es.alfonsomarin.finances.report;

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.util.PathsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * The type Report writer factory.
 *
 * @author alfonso.marin.lopez
 */
@Component
public class ReportWriterFactory {
    
    private ApplicationContext context;
    
    private ReportConfiguration reportConfiguration;
    
    private PathsUtils pathsUtils;
    
    private MessageSource messageSource;




    /**
     * Get insert writer report writer.
     *
     * @param insertRequest the insert request
     * @return the report writer
     */
    public ReportWriter getInsertWriter(InsertRequest insertRequest){
        return context.getBean(InsertLog4JReportWriter.class, 
                reportConfiguration,
                pathsUtils,
                insertRequest,
                messageSource);
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Sets report configuration.
     *
     * @param reportConfiguration the report configuration
     */
    @Autowired
    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
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
     * Sets message source.
     *
     * @param messageSource the message source
     */
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
