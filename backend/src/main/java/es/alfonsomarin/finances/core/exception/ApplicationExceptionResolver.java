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

import es.alfonsomarin.finances.core.domain.common.exception.ExceptionDetail;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>Application {@code ExceptionResolver} that builds controlled error responses.</p>
 *
 * @author alfonso.marin.lopez
 * @see ExceptionResolver
 * @see ExceptionDetail
 * @see ExceptionType
 */
@Component
class ApplicationExceptionResolver extends DefaultExceptionResolver {

    /**
     * Resolves controlled error responses, explicitly thrown based
     * on business logic.
     *
     * @param ex Instance of {@code ApplicationException}
     * @return Error response.
     */
    @Override
    public ExceptionDetail resolveErrorResponse(Exception ex) {

        final ExceptionType exceptionType = ((ApplicationException) ex).getExceptionType();
        final int code = ((ApplicationException) ex).getCode();
        final Object[] resources = ((ApplicationException) ex).getResources();
        Map<String,String> details = new HashMap<>();
        details.put("exception", ex.getMessage());
        return new ExceptionDetail(
                getFormattedMessage(code, resources), 
                code, 
                exceptionType.severity(),
                details);
    }
}
