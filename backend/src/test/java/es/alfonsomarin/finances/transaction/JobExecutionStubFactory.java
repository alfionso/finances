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

package es.alfonsomarin.finances.transaction;

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.util.Constants;
import org.springframework.batch.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author alfonso.marin.lopez
 */
public class JobExecutionStubFactory {

    /**
     * Generate job execution entity.
     *
     * @return the job execution entity
     */
    public static JobExecution generateJobExecutionEntity(Long requestId, BatchStatus status){
        JobExecution jobExecution = new JobExecution(1L, generateJobParameters(requestId));
        jobExecution.setStatus(status);
        return jobExecution;
    }

    /**
     * Generate job execution parameters.
     *
     * @return executions parameters
     */
    public static JobParameters generateJobParameters(Long requestId){
        return new JobParametersBuilder()
                .addString(Constants.JOB_PARAM_PATH_FILE, ".")
                .addString(Constants.JOB_PARAM_ID_INSERT_REQUEST, requestId.toString())
                .addLong("time", new Date().getTime())
                .toJobParameters();
    }

    /**
     * Generate job instance list.
     *
     * @return the list
     */
    public static List<JobInstance> generateJobInstanceList() {
        return new ArrayList<>(Arrays.asList( new JobInstance(TestConstants.JOB_INSTANCE_ID, TestConstants.JOB_INSTANCE_NAME)));
    }
}
