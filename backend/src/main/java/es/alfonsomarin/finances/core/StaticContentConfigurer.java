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

package es.alfonsomarin.finances.core;

import es.alfonsomarin.finances.report.ReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * {@code Configuration} class that enables the embedded servlet container
 * to serve static content from the location specified in the application
 * properties ({@code static.content.location}).
 * <p>
 * If the property is not set, this configuration is bypassed, which means
 * that static content is being served by an external web server.
 *
 * @author alfonso.marin.lopez
 * @see Configuration
 * @see ConditionalOnProperty
 */
@Configuration
@ConditionalOnProperty(prefix = "static.content", name="location")
class StaticContentConfigurer extends WebMvcConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FILE = "file:";
    private static final String PATH_RESOURCES = "/**";
    private static final String METADATA_RESOURCES = "/metadata/**";

    @Value("${static.content.location}")
    private String staticContentLocation;
    
    private ReportConfiguration reportConfiguration;
    private ApplicationConfig applicationConfig;
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/public/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(!registry.hasMappingForPattern("/"+ReportConfiguration.REPORT_FOLDER_NAME+"/**")){
            logger.info("Static content for reports at : [{}]",reportConfiguration.getLogFolder()+"/"+ReportConfiguration.REPORT_FOLDER_NAME );
            registry.addResourceHandler("/"+ReportConfiguration.REPORT_FOLDER_NAME+"/**").addResourceLocations(
                    FILE.concat(reportConfiguration.getLogFolder()
                    .concat(ReportConfiguration.REPORT_FOLDER_NAME).concat("/")));
        }
        if(!registry.hasMappingForPattern(METADATA_RESOURCES)){
            logger.info("Static content for metadata files at: [{}]", applicationConfig.getInsertFolder());
            registry.addResourceHandler(METADATA_RESOURCES).addResourceLocations(
                    FILE.concat(applicationConfig.getInsertFolder())
            );
        }
        if(!registry.hasMappingForPattern(PATH_RESOURCES)){
            logger.info("Static content being served by the embedded servlet container at: [{}]", staticContentLocation);
            registry.addResourceHandler(PATH_RESOURCES).addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS[0], FILE.concat(staticContentLocation));
        }
    }

    /**
     * Set report configuration.
     *
     * @param reportConfiguration the report configuration
     */
    @Autowired
    public void setReportConfiguration(ReportConfiguration reportConfiguration){
        this.reportConfiguration = reportConfiguration;
    }

    /**
     * Set application config.
     *
     * @param applicationConfig the application config
     */
    @Autowired
    public void setApplicationConfig(ApplicationConfig applicationConfig){
        this.applicationConfig = applicationConfig;
    }
}