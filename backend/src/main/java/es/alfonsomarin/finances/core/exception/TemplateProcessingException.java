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

import es.alfonsomarin.finances.core.template.TemplateService;

/**
 * <p>Wrapper exception for exceptions thrown by {@code TemplateService} implementations.</p>
 *
 * @author alfonso.marin.lopez
 * @see TemplateService
 */
public class TemplateProcessingException extends Exception {

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the              {@link #getCause()} method).  (A <tt>null</tt> value is              permitted, and indicates that the cause is nonexistent or              unknown.)
     * @since 1.4
     */
    public TemplateProcessingException(Throwable cause) {
        super(cause);
    }
}
