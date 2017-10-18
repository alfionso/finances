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

package es.alfonsomarin.finances.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionDetail;

/**
 * {@code ExceptionResolver} interface that declares a method to resolve
 * error responses as well as defines a default method to transform it
 * to JSON as expected by the API contract.
 *
 * @author alfonso.marin.lopez
 */
interface ExceptionResolver {

    /**
     * Returns the exception details specified in the API contract.
     *
     * @param ex Exception to be resolved.
     * @return Exception details.
     */
    ExceptionDetail resolveErrorResponse(Exception ex);

    /**
     * In-memory instance of {@code ObjectMapper}
     */
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Default implementation to convert exception details to JSON.
     *
     * @param exceptionDetail Exception details
     * @return Json representation of {@code ExceptionDetail}
     */
    default String toJsonResponse(ExceptionDetail exceptionDetail) {
        try {
            return objectMapper.writer()
                    .writeValueAsString(
                            exceptionDetail
                    );
        } catch (JsonProcessingException e) {
            return exceptionDetail.toString();
        }
    }
}
