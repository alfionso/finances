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

import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.nio.file.Path;

/**
 * The type Upload log 4 j report writer.
 *
 * @author alfonso.marin.lopez
 */
@Repository
@Scope("prototype")
public class InsertLog4JReportWriter extends Log4JReportWriter {
    private static final Logger LOGGER = LogManager.getLogger(InsertLog4JReportWriter.class);

    private Logger loggerSuccess;

    private Logger loggerError;
    
    
    private InsertRequest insertRequest;

    /**
     * Instantiates a new Insert log 4 j report writer.
     *
     * @param reportConfiguration the report configuration
     * @param pathsUtils          the paths utils
     * @param insertRequest       the insert request
     */
    public InsertLog4JReportWriter(ReportConfiguration reportConfiguration,
                                   PathsUtils pathsUtils,
                                   InsertRequest insertRequest,
                                   MessageSource messageSource){
        super(reportConfiguration, pathsUtils,messageSource);
        this.insertRequest = insertRequest;
      
        Path pathFile = getPathsUtils()
                .relativePathInsertSuccess(this.insertRequest.getId());
        this.loggerSuccess = getLogger(pathFile,false);
        setHeaderIfEmpty(pathsUtils.absolutePathReports(pathFile), loggerSuccess);
        
        pathFile = getPathsUtils()
                .relativePathInsertError(this.insertRequest.getId());
        this.loggerError = getLogger(pathFile,false);
        setHeaderIfEmpty(pathsUtils.absolutePathReports(pathFile), loggerError);
    }
    
    private void setHeaderIfEmpty(Path pathToFile, Logger logger){

        try {
            File file = new File(pathToFile.toString());
            if(file.length()<10){ // 10 characters, may be spaces or null characters
                logger.info(
                        getMessageSource().getMessage(
                                Constants.MESSAGE.concat(Constants.DOT).concat(String.valueOf(MessageCodes.CODE_METADATA_HEADER)),
                                null, LocaleContextHolder.getLocale())
                );
            }
        } catch (Exception e) {
            // Nothing to do
            LOGGER.warn(e);
        }
    }

    @Override
    public void error(String message) {
        loggerError.error("["+ReportConfiguration.LOG_ERROR_FILENAME + "-" + this.insertRequest.getId() + "-ERROR]"+message);
    }

    @Override
    public void error(MetadataRecord metadataRecord) {
        loggerError.error(toLine(metadataRecord));
    }

    @Override
    public void info(String message) {
        loggerSuccess.info("["+ReportConfiguration.LOG_ERROR_FILENAME+ "-" + this.insertRequest.getId() + "-INFO] - " +message);
    }

    @Override
    public void info(MetadataRecord metadataRecord){
        loggerSuccess.info(toLine(metadataRecord));
    }
    
}
