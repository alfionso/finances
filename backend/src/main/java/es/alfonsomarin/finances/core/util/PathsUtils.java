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

package es.alfonsomarin.finances.core.util;

import es.alfonsomarin.finances.core.ApplicationConfig;
import es.alfonsomarin.finances.report.ReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Paths utils.
 *
 * @author alfonso.marin.lopez
 */
@Component
public class PathsUtils {
    
    private ReportConfiguration reportConfiguration;
    
    private ApplicationConfig applicationConfig;


    /**
     * Relative path upload log path.
     *
     * @param id the id
     * @return the path
     */
    public Path relativePathInsertSuccess(Long id){
        return Paths.get(
                ReportConfiguration.REPORT_FOLDER_NAME,
                ReportConfiguration.INSERT_FOLDER_NAME,
                id.toString(), 
                ReportConfiguration.LOG_SUCCESSFULL_FILENAME);
    }

    /**
     * Relative path insert error path.
     *
     * @param id the id
     * @return the path
     */
    public Path relativePathInsertError(Long id){
        return Paths.get(
                ReportConfiguration.REPORT_FOLDER_NAME,
                ReportConfiguration.INSERT_FOLDER_NAME,
                id.toString(),
                ReportConfiguration.LOG_ERROR_FILENAME);
    }
    
    public Path relativePathInsert(Long id, String fileName){
        return Paths.get(
                applicationConfig.getInsertFolder(),
                id.toString(),
                fileName);
    }


    /**
     * Absolute path to save the csv file on an insert request
     * @param id
     * @param fileName
     * @return
     */
    public Path absolutePathInsert(Long id, String fileName){
        return Paths.get(
                applicationConfig.getInsertFolder(),
                id.toString()).resolve(fileName);
    }

    /**
     * Absolute path reports path.
     *
     * @param relativePath the relative path
     * @return the path
     */
    public Path absolutePathReports(Path relativePath){
        return Paths.get(reportConfiguration.getLogFolder())
                .resolve(relativePath);
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
