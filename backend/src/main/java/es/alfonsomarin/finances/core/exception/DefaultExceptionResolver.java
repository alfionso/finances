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
import es.alfonsomarin.finances.core.util.Constants;
import freemarker.template.TemplateException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;


/**
 * <p>Default {@code ExceptionResolver} that does not require a specific treatment
 * in order to generate the exception information to be sent to API clients.</p>
 * <p>
 * <p>It resolves the {@code ExceptionType} based on the exception class name (defaults
 * to "UNEXPECTED"), which allows this class to properly build error responses (with
 * message, code, response status and severity of the exception.</p>
 *
 * @author alfonso.marin.lopez
 * @see ExceptionResolver
 * @see ExceptionDetail
 * @see ExceptionType
 */
@Component
class DefaultExceptionResolver implements ExceptionResolver {
    
    private MessageSource messageSource;

    /**
     * In-memory mapping of exception types.
     */
    private Map<String, ExceptionType> errorMappings = new HashMap<>();

    /**
     * Initialize mapping.
     */
    @PostConstruct
    public void init() {

        // VALIDATION - 400
        errorMappings.put(MissingServletRequestPartException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(HttpMessageNotReadableException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(BindException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(IllegalArgumentException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(ValidationException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(InvalidDataAccessApiUsageException.class.getSimpleName(), ExceptionType.VALIDATION);
        errorMappings.put(ApplicationException.class.getSimpleName(), ExceptionType.SECURITY);

      
        // NOT_ALLOWED - 405
        errorMappings.put(HttpRequestMethodNotSupportedException.class.getSimpleName(), ExceptionType.NOT_ALLOWED);

        // DATA_INTEGRITY - 409
        errorMappings.put(TemplateException.class.getSimpleName(), ExceptionType.DATA_INTEGRITY);
        errorMappings.put(ConstraintViolationException.class.getSimpleName(), ExceptionType.DATA_INTEGRITY);
        errorMappings.put(DataIntegrityViolationException.class.getSimpleName(), ExceptionType.DATA_INTEGRITY);


    }

    /**
     * Resolves standard exception details based on the {@code ExceptionType}.
     *
     * @param ex {@code Exception} thrown.
     * @return Exception details.
     */
    @Override
    public ExceptionDetail resolveErrorResponse(Exception ex) {

        final ExceptionType exceptionType = ofNullable(
                errorMappings.get(ex.getClass().getSimpleName())
        ).orElse(ExceptionType.UNEXPECTED);
        Map<String, String> details = new HashMap<>();
        details.put("exception",ex.getMessage());
        return new ExceptionDetail(
                getFormattedMessage(exceptionType.code()) + "\n-" +ex.getMessage(),
                exceptionType.code(), 
                exceptionType.severity(), 
                details
        );
    }

    /**
     * Formats the exception detailed message.
     *
     * @param code      Message code.
     * @param resources Resources to be interpolated into the message.
     * @return Formatted message.
     */
    protected String getFormattedMessage(int code, Object... resources) {

        return messageSource.getMessage(
                Constants.MESSAGE.concat(Constants.DOT).concat(String.valueOf(code)),
                resources, LocaleContextHolder.getLocale()
        );
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
}
