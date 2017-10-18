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

import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;

/**
 * Finances exception. It contains all attributes needed in order
 * to provide contextual error responses to API clients.
 *
 * @author alfonso.marin.lopez
 */
public class ApplicationException extends RuntimeException {

    /**
     * Error message code.
     */
    private final int code;

    /**
     * Resources used to build the error message.
     */
    private final Object[] resources;

    /**
     * Exception type, provides context data for API response.
     */
    private final ExceptionType exceptionType;

    /**
     * Exception constructor.
     *
     * @param message       Error message.
     * @param ex            Exception cause.
     * @param code          Error message code.
     * @param resources     Resources used to compose error messages.
     * @param exceptionType Exception type (see {@code ExceptionType})
     */
    public ApplicationException(String message, Throwable ex, int code, Object[] resources, ExceptionType exceptionType) {
        super(message, ex);
        this.code = code;
        this.exceptionType = exceptionType;

        Object[] copy = null;
        if (resources != null) {
            copy = new Object[resources.length];
            System.arraycopy(resources, 0, copy, 0, resources.length);
        }
        this.resources = copy;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets exception type.
     *
     * @return the exception type
     */
    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    /**
     * Get resources object [ ].
     *
     * @return the object [ ]
     */
    public Object[] getResources() {
        Object[] copy = null;
        if (resources != null) {
            copy = new Object[resources.length];
            System.arraycopy(resources, 0, copy, 0, resources.length);
        }
        return copy;
    }
}
