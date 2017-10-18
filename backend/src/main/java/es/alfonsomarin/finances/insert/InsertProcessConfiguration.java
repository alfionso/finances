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

package es.alfonsomarin.finances.insert;

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.report.ReportConfiguration;
import es.alfonsomarin.finances.report.ReportWriter;
import es.alfonsomarin.finances.report.ReportWriterFactory;
import es.alfonsomarin.finances.transaction.TransactionService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * The type Insert process configuration.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@Lazy
public class InsertProcessConfiguration {
    private static final Logger LOGGER = LogManager.getLogger(InsertProcessConfiguration.class);

    /**
     * The Job builder factory.
     */
    private JobBuilderFactory jobBuilderFactory;

    /**
     * The Step builder factory.
     */
    private StepBuilderFactory stepBuilderFactory;

    /**
     * The Report configuration.
     */
    private ReportConfiguration reportConfiguration;

    /**
     * The Report writer factory.
     */
    public ReportWriterFactory reportWriterFactory;

    /**
     * The Insert service.
     */
    private InsertService insertService;

    private TransactionService transactionService;

    private MessageSource messageSource;

    /**
     * Insert reader flat file item reader.
     *
     * @param pathToFile the path to file
     * @return the flat file item reader
     */
    @Bean
    @StepScope//mandatory to get the job parameters
    public FlatFileItemReader<MetadataRecord> insertReader(@Value("#{jobParameters[" + Constants.JOB_PARAM_PATH_FILE + "]}") String pathToFile) {
        LOGGER.debug("Reading file " + pathToFile);
        FlatFileItemReader<MetadataRecord> reader = new InsertItemReader(
                pathToFile, 
                reportConfiguration.getLinesHeader(), 
                reportConfiguration.getCsvSeparator());
        return reader;
    }

    /**
     * Insert processor item processor.
     *
     * @param pathToFile the path to file
     * @return the item processor
     */
    @Bean
    @StepScope
    public ItemProcessor insertProcessor(@Value("#{jobParameters[" + Constants.JOB_PARAM_PATH_FILE + "]}") String pathToFile) {
        return new InsertProcessor(pathToFile);
    }

    /**
     * Insert writer item writer.
     *
     * @param idRequest     the id request
     * @param stepExecution the step execution
     * @return the item writer
     */
    @Bean
    @StepScope //mandatory to get the job parameters
    public ItemWriter<InsertResult> insertWriter(@Value("#{jobParameters[" + Constants.JOB_PARAM_ID_INSERT_REQUEST + "]}") Long idRequest,
                                                 @Value("#{stepExecution}") StepExecution stepExecution) {

        InsertRequest insertRequest = insertService.getInsertRequest(idRequest);
        ReportWriter reportWriter = reportWriterFactory.getInsertWriter(insertRequest);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(reportConfiguration.getDateFormat());
        LOGGER.debug("Writer value id request:" + idRequest);
        ItemWriter<InsertResult> itemWriter = list -> list.forEach(insertResult -> {
            MetadataRecord record = insertResult.getMetadataRecord();
            if(insertResult.isInserted()){
                Transaction transaction = new Transaction()
                        .description(record.getDescription());
                try {
                    transaction.setDateCreation(simpleDateFormat.parse(record.getDate()));
                } catch (ParseException e) {
                    // nothing to do
                }
                try {
                    transaction.setValue(NumberUtils.createBigDecimal(record.getValue()));
                } catch (NumberFormatException e) {
                    // nothing to do
                }
                transactionService.save(transaction);
                insertRequest.addSuccess();
                reportWriter.info(insertResult.getMetadataRecord());
            } else {
                insertRequest.addError();
                reportWriter.error(insertResult.getMetadataRecord());
            }
            insertService.updateInsertRequest(insertRequest);
        });
        return itemWriter;
    }


    /**
     * Insertion end listener job execution listener support.
     *
     * @return the job execution listener support
     */
    @Bean
    public JobExecutionListenerSupport insertionEndListener() {
        return new JobExecutionListenerSupport();
    }


    /**
     * Insert metadata file job job.
     *
     * @param listener the listener
     * @return the job
     */
    @Bean(name = "insertJob")
    public Job insertMetadataFileJob(InsertRequestValidationEndListener listener) {
        return jobBuilderFactory.get("insertMetadataFileJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stepInsert())
                .end()
                .build();
    }

    /**
     * Step insert step.
     *
     * @return the step
     */
    @Bean
    public Step stepInsert() {
        return stepBuilderFactory.get("stepInsert")
                .<MetadataRecord, MetadataRecord>chunk(1)
                .reader(insertReader(null))
                .processor(insertProcessor(null))
                .writer(insertWriter(null, null))
                .build();
    }

    /**
     * Sets job builder factory.
     *
     * @param jobBuilderFactory the job builder factory
     */
    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    /**
     * Sets step builder factory.
     *
     * @param stepBuilderFactory the step builder factory
     */
    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
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
     * Sets report writer factory.
     *
     * @param reportWriterFactory the report writer factory
     */
    @Autowired
    public void setReportWriterFactory(ReportWriterFactory reportWriterFactory) {
        this.reportWriterFactory = reportWriterFactory;
    }

    /**
     * Sets insert service.
     *
     * @param insertService the insert service
     */
    @Autowired
    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
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

    /**
     * Sets transaction service.
     *
     * @param transactionService the transaction service
     */
    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
