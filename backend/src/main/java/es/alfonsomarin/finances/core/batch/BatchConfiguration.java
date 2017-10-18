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
package es.alfonsomarin.finances.core.batch;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Batch configuration
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    
    /**
     * The Job repository.
     */
    private JobRepository jobRepository;
    
    /**
     * The Job registry.
     */
    private JobRegistry jobRegistry;
    
    /**
     * The Job launcher.
     */
    private JobLauncher jobLauncher;
    
    /**
     * The Job explorer.
     */
    private JobExplorer jobExplorer;

    /**
     * Job operator job operator.
     *
     * @return the job operator
     */
    @Bean
    public JobOperator jobOperator() {
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobRepository(jobRepository);
        return jobOperator;
    }

    /**
     * This is necessary to start a job with jobOperation.
     * Without this configuration we will have a error like "No job configuration with the name [XXXX] was registered"
     *
     * @param jobRegistry the job registry
     * @return job registry bean post processor
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    /**
     * Sets job repository.
     *
     * @param jobRepository the job repository
     */
    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Sets job registry.
     *
     * @param jobRegistry the job registry
     */
    @Autowired
    public void setJobRegistry(JobRegistry jobRegistry) {
        this.jobRegistry = jobRegistry;
    }

    /**
     * Sets job launcher.
     *
     * @param jobLauncher the job launcher
     */
    @Autowired
    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    /**
     * Sets job explorer.
     *
     * @param jobExplorer the job explorer
     */
    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
}
