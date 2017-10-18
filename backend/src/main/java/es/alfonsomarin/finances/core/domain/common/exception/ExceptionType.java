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

import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_ACCESS_DENIED;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_AUTH_GENERIC;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_AUTH_LOGIN;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_AUTH_LOGOUT;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_AUTH_SESSION;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_BATCH_NOT_FOUND;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_DATA_INTEGRITY;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_NOT_ALLOWED;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_SECURITY_ERRORS;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_UNEXPECTED_ERROR;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_VALIDATION_ERRORS;

/**
 * Enumeration that lists possible exception types.
 *
 * @author alfonso.marin.lopez
 */
public enum ExceptionType {


    /**
     * Unexpected exception
     */
    UNEXPECTED(Severity.ERROR, CODE_UNEXPECTED_ERROR),

    /**
     * API method not allowed
     */
    NOT_ALLOWED(Severity.ERROR, CODE_NOT_ALLOWED),

    /**
     * A business validation error
     */
    VALIDATION(Severity.WARNING, CODE_VALIDATION_ERRORS),

    /**
     * A security validation error
     */
    SECURITY(Severity.ERROR, CODE_SECURITY_ERRORS),

    /**
     * A data constraint/integrity error
     */
    DATA_INTEGRITY(Severity.WARNING, CODE_DATA_INTEGRITY),

    /**
     * A requested resource doesn't exist
     */
    RESOURCE_NOT_FOUND(Severity.INFORMATION, CODE_DATA_INTEGRITY),

    /**
     * The user is not granted to access the resource
     */
    AUTHORIZATION(Severity.ERROR, CODE_ACCESS_DENIED),

    /**
     * The user is not authenticated
     */
    AUTHENTICATION(Severity.INFORMATION, CODE_AUTH_GENERIC),

    /**
     * Wrong login credentials
     */
    LOGIN(Severity.WARNING, CODE_AUTH_LOGIN),

    /**
     * The user has logged out
     */
    LOGOUT(Severity.WARNING, CODE_AUTH_LOGOUT),

    /**
     * The session is expired
     */
    SESSION_EXPIRED(Severity.WARNING, CODE_AUTH_SESSION),

    /**
     * Batch ejecution exception type.
     */
    BATCH_EJECUTION(Severity.WARNING, CODE_BATCH_NOT_FOUND);
    
    private final Severity severity;
    private final int code;

    ExceptionType(Severity severityType, int errorCode) {
        this.severity = severityType;
        this.code = errorCode;
    }

    /**
     * Severity string.
     *
     * @return the string
     */
    public String severity() {
        return severity.toString();
    }

    /**
     * Code int.
     *
     * @return the int
     */
    public int code() {
        return code;
    }

    /**
     * Enumeration that describes the severityType of the exception type
     */
    private enum Severity {
        /**
         * Information severity.
         */
        INFORMATION(1),
        /**
         * Warning severity.
         */
        WARNING(2),
        /**
         * Error severity.
         */
        ERROR(3);

        private final int value;

        Severity(int value) {
            this.value = value;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }
}
