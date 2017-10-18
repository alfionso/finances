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

package es.alfonsomarin.finances.core.domain.common.exception;

import java.util.Map;

/**
 * Exception detail notification model for API clients
 *
 * @author alfonso.marin.lopez
 */
public class ExceptionDetail {

    private final String message;

    private final int code;

    private final String severity;

    private final Map<String, String> details;

    /**
     * Instantiates a new Exception detail.
     *
     * @param message  the message
     * @param code     the code
     * @param severity the severity
     * @param details  the details
     */
    public ExceptionDetail(final String message, final int code, String severity, Map<String, String> details) {
        this.message = message;
        this.code = code;
        this.severity = severity;
        this.details = details;
    }

    /**
     * Instantiates a new Exception detail.
     *
     * @param message       the message
     * @param exceptionType the exception type
     * @param details       the details
     */
    public ExceptionDetail(final String message, final ExceptionType exceptionType, Map<String, String> details) {
        this.message = message;
        this.code = exceptionType.code();
        this.severity = exceptionType.severity();
        this.details = details;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public final int getCode() {
        return code;
    }

    /**
     * Gets severity.
     *
     * @return the severity
     */
    public final String getSeverity() {
        return severity;
    }

    /**
     * Gets details.
     *
     * @return the details
     */
    public final Map<String, String> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "\tException: " + message + '\n' +
                "\tCode     : " + code + '\n' +
                "\tSeverity     : " + severity + '\n' +
                "\tDetails  : " + details.toString() + '\n';
    }
    
    
}