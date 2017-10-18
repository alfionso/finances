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

import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

/**
 * The type Log 4 j report writer.
 *
 * @author alfonso.marin.lopez
 */
public abstract class Log4JReportWriter implements ReportWriter {
    private static final Logger LOGGER = LogManager.getLogger(Log4JReportWriter.class);
    
    
    private ReportConfiguration reportConfiguration;
    
    private PathsUtils pathsUtils;
    
    private MessageSource messageSource;

    /**
     * Instantiates a new Log 4 j report writer.
     *
     * @param reportConfiguration the report configuration
     * @param pathsUtils          the paths utils
     * @param messageSource       the message source
     */
    public Log4JReportWriter(ReportConfiguration reportConfiguration, 
                             PathsUtils pathsUtils,
                             MessageSource messageSource){
        this.reportConfiguration = reportConfiguration;    
        this.pathsUtils = pathsUtils;
        this.messageSource = messageSource;
    }

    /**
     * Get report configuration report configuration.
     *
     * @return the report configuration
     */
    protected ReportConfiguration getReportConfiguration(){
        return reportConfiguration;
    }

    /**
     * Get paths utils paths utils.
     *
     * @return the paths utils
     */
    protected PathsUtils getPathsUtils(){
        return this.pathsUtils;
    }

    /**
     * Get message source message source.
     *
     * @return the message source
     */
    protected MessageSource getMessageSource(){
        return messageSource;
    }

    /**
     * Get logger logger.
     *
     * @param fileName  the file name
     * @param cleanFile the clean file
     * @return the logger
     */
    protected Logger getLogger(Path fileName, boolean cleanFile){
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        Layout layout = PatternLayout.createLayout("%m%n", null, config, null,
                null, true, true,null, null);
        Path pathToFile = pathsUtils.absolutePathReports(fileName);
        if(cleanFile){
            cleanFile(pathToFile);    
        }
        LOGGER.info("Log4j report to the path["+pathToFile.toString()+"]");
        final Appender appender = FileAppender.newBuilder()
                .withFileName(pathToFile.toString())
                .withName("File")
                .withConfiguration(config)
                .withLayout(layout)
                .build();
        appender.start();
        config.addAppender(appender);

        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(
                false, Level.INFO, "org.apache.logging.log4j",
                "true", refs, null, config, null );
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(fileName.toString(), loggerConfig);
        ctx.updateLoggers();
        return ctx.getLogger(fileName.toString());
    }

    /**
     * To line string.
     *
     * @param metadataRecord the metadata record
     * @return the string
     */
    protected String toLine(MetadataRecord metadataRecord){
        String sep = reportConfiguration.getCsvSeparator();
        StringBuffer line = new StringBuffer();
        line.append(metadataRecord.getDate()).append(sep);
        line.append(metadataRecord.getDescription()).append(sep);
        line.append(metadataRecord.getValue());
        if(StringUtils.hasText(metadataRecord.getError())){
            line.append(sep);
            line.append(metadataRecord.getError());
        }

        return line.toString();
    }

    private void cleanFile(Path pathToFile){
        try {
            File file = new File(pathToFile.toString());
            if(file.exists()){
                FileChannel outChan = new FileOutputStream(pathToFile.toString(), true).getChannel();
                outChan.truncate(0L);
                outChan.close();
            }
        } catch (IOException e) {
            // Nothing to do
            LOGGER.warn(e);
        }
    }
    
}
