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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.io.File;
import java.nio.file.Paths;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class Log4JReportWriterTest {

    /**
     * The Report configuration.
     */
    ReportConfiguration reportConfiguration;
    /**
     * The Paths utils.
     */
    PathsUtils pathsUtils;
    /**
     * The insert request.
     */
    InsertRequest insertRequest;
    
    @Mock
    MessageSource messageSource;
    /**
     * The Log 4 j report writer.
     */
    Log4JReportWriter log4JReportWriter;
    private final String TEST_TEXT= "test1";
    private final String PATH_LOGS= "./target/";
    private final Long ID_REQUEST = 1L;
    private final String FILE_NAME= "sample";


    /**
     * Clean.
     */
    @Before
    public void clean(){
        pathsUtils = new PathsUtils();
        File fileDirectory = new File(Paths.get(PATH_LOGS,ReportConfiguration.REPORT_FOLDER_NAME).toUri());
        if(fileDirectory.exists()){
            fileDirectory.delete();
        }
        reportConfiguration = new ReportConfiguration();
        reportConfiguration.setLinesHeader(1);
        
        pathsUtils.setReportConfiguration(reportConfiguration);
        reportConfiguration.setLogFolder(PATH_LOGS);
        insertRequest = new InsertRequest();
        insertRequest.setId(ID_REQUEST);
        insertRequest.setFileName(FILE_NAME);
        when(messageSource.getMessage(anyString(),anyObject(),anyString(),anyObject())).thenReturn("Message");
        log4JReportWriter = new InsertLog4JReportWriter(reportConfiguration, pathsUtils, insertRequest, messageSource);
    }

    /**
     * Info then file create test.
     */
    @Test
    public void infoThenFileCreateTest(){
        log4JReportWriter.info(TEST_TEXT);
        File fileLog = new File(Paths.get(PATH_LOGS, pathsUtils.relativePathInsertSuccess(ID_REQUEST).toString()).toUri());
        Assert.assertTrue("Successfull file doesn't exist", fileLog.exists());
        
    }


    /**
     * Error then file create test.
     */
    @Test
    public void errorThenFileCreateTest(){
        log4JReportWriter.error(TEST_TEXT);
        File fileLog = new File(Paths.get(PATH_LOGS, pathsUtils.relativePathInsertError(ID_REQUEST).toString()).toUri());        
        Assert.assertTrue("Error file doesn't exist", fileLog.exists());

    }
 
}
