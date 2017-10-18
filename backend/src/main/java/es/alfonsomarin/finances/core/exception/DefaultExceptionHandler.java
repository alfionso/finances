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
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * <p>The {@code DefaultExceptionHandler} is the default exception handling strategy
 * of the application.</p>
 * <p>
 * <p>Exceptions are dealt with as being simple feedback {@code ExceptionDetail} provided
 * to API clients. Expected exceptions are only logged as WARN level and do not clutter
 * the logs with the stacktrace.</p>
 * <p>
 * <p>The only "exceptions" to this rule are exceptions of ERROR level, which print stacktrace.</p>
 * <p>
 * <p>Its responsibilities are:</p>
 * <ul>
 * <li>Audit exception.</li>
 * <li>Resolve the error response.</li>
 * </ul>
 *
 * @author alfonso.marin.lopez
 * @see ExceptionResolver
 * @see ExceptionDetail
 */
@Component
public class DefaultExceptionHandler extends AbstractHandlerMethodExceptionResolver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Class constructor.
     */
    public DefaultExceptionHandler() {
        setOrder(HIGHEST_PRECEDENCE);
    }
    
    private ExceptionResolverFactory exceptionResolverFactory;

    /**
     * Resolves the error response to API clients.
     *
     * @param request API request
     * @param response API response
     * @param handlerMethod context of application's method which caused
     *                      exception to be thrown
     * @param ex {@code Exception} thrown
     * @return empty {@code ModelAndView}, causes the application to produce a response
     */
    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {

        final ExceptionResolver exceptionResolver = exceptionResolverFactory
                .getExceptionResolverInstance(ex.getClass().getName());
        final ExceptionDetail exceptionDetail = exceptionResolver.resolveErrorResponse(ex);

        try {
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().print(
                    exceptionResolver.toJsonResponse(exceptionDetail)
            );
        } catch (IOException e) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }

        auditException(ex, exceptionDetail.getSeverity(), handlerMethod);

        return new ModelAndView();
    }

    /**
     * Override default behaviour to prevent logging the stacktrace for
     * warning and information exception severities.
     *
     * @param ex      the exception that got thrown during handler validate
     * @param request current HTTP request (useful for obtaining metadata)
     *
     * @see Logger#error(String, Throwable)
     * @see Logger#warn(String, Object)
     * @see Logger#info(String, Object)
     */
    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        // Do nothing
    }

    private void auditException(Exception exception, String type, HandlerMethod handlerMethod) {

        final String exceptionContext = " - [Method: " + getMethodName(handlerMethod) + "]" +
                " / [Class: " + getBeanName(handlerMethod) + "]";

        switch (type) {
            case "ERROR":
                logger.error(exception.getMessage(), exception);
                break;

            case "WARNING":
                logger.warn(exception.getMessage().concat("{}"), exceptionContext);
                break;

            default:
                logger.info(exception.getMessage().concat("{}"), exceptionContext);
                break;
        }
    }

    private String getMethodName(HandlerMethod handlerMethod) {
        if (Optional.ofNullable(handlerMethod).isPresent()) {
            return handlerMethod.getMethod().getName();
        }
        return Constants.UNKNOWN;
    }

    private String getBeanName(HandlerMethod handlerMethod) {
        if (Optional.ofNullable(handlerMethod).isPresent()) {
            return ReflectionUtils.unwrapProxy(handlerMethod.getBean())
                    .getClass().getSimpleName();
        }
        return Constants.UNKNOWN;
    }

    /**
     * Sets exception resolver factory.
     *
     * @param exceptionResolverFactory the exception resolver factory
     */
    @Autowired
    public void setExceptionResolverFactory(ExceptionResolverFactory exceptionResolverFactory) {
        this.exceptionResolverFactory = exceptionResolverFactory;
    }
}
