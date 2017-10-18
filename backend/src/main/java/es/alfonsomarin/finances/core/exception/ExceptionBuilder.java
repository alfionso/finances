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
import org.springframework.util.Assert;

/**
 * {@code ApplicationException} builder.
 *
 * @author alfonso.marin.lopez
 */
public class ExceptionBuilder {

    /**
     * Exception message.
     */
    private String message;

    /**
     * Exception type, needed to build API error responses.
     */
    private ExceptionType exceptionType;

    /**
     * Exception cause (optional).
     */
    private Throwable exception;

    /**
     * Exception message code (optional).
     */
    private int code;

    /**
     * Resources to compose an exception message (optional).
     */
    private Object[] resources;

    /**
     * Provides an exception message.
     *
     * @param message Exception message.
     * @return Instance of {@code ExceptionBuilder}
     */
    public ExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Provides an exception type.
     *
     * @param exceptionType Exception type.
     * @return Instance of {@code ExceptionBuilder}
     */
    public ExceptionBuilder exceptionType(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
        return this;
    }

    /**
     * Provides an exception cause.
     *
     * @param exception Exception cause.
     * @return Instance of {@code ExceptionBuilder}
     */
    public ExceptionBuilder exception(Exception exception) {
        this.exception = exception;
        return this;
    }

    /**
     * Provides a exception message code.
     *
     * @param code Exception message code.
     * @return Instance of {@code ExceptionBuilder}
     */
    public ExceptionBuilder code(int code) {
        this.code = code;
        return this;
    }

    /**
     * Provides resources needed to compose the exception message.
     *
     * @param resources Array of resources used to compose messages.
     * @return Instance of {@code ExceptionBuilder}
     */
    public ExceptionBuilder resources(Object... resources) {
        this.resources = resources;
        return this;
    }

    /**
     * Builds an instance of {@code ApplicationException}.
     *
     * @return ApplicationException application exception
     */
    public ApplicationException build() {
        Assert.notNull(message, "Message must not be null.");
        Assert.notNull(exceptionType, "ExceptionType must not be null.");

        final int exCode = this.code > 0 ? this.code : exceptionType.code();
        return new ApplicationException(
                message, exception, exCode, resources, exceptionType
        );
    }

    /**
     * Throws the exception being built (to be used in {@code Consumer} lambdas).
     *
     * @throws ApplicationException
     */
    public void fail() {
        throw build();
    }
}