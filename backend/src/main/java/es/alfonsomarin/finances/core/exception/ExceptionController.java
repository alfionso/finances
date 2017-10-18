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

import com.google.common.base.CharMatcher;
import es.alfonsomarin.finances.core.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Custom implementation for Spring's default servlet error controller.</p>
 * <p>
 * <p>When clients request an URI for which there's no suitable mapping or
 * static resource, this code redirects the call to <i>#/original-URI</i>,
 * this way delegating the error handling to the front end client.</p>
 * <p>
 * <p>When the error is related to maximum upload size throw by undertow
 * the default exception handler is called in order to generate a suitable
 * response
 * <p/>
 * <p>Eventually, what the font end does is to display a generic error page
 * displaying a message "The requested resource was not found."</p>
 *
 * @author alfonso.marin.lopez
 */
@Controller
public class ExceptionController extends AbstractErrorController {


    /**
     * The Multipart max upload size code.
     */
    private final String MULTIPART_MAX_UPLOAD_SIZE_CODE = "UT000054";

    private DefaultExceptionHandler exceptionHandler;

    /**
     * Instantiates a new Exception controller.
     *
     * @param exceptionHandler the exception handler
     */
    @Autowired
    public ExceptionController(DefaultExceptionHandler exceptionHandler) {
        super(new DefaultErrorAttributes());
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Default behaviour when the container cannot find a suitable
     * API mapping.
     *
     * @param request  Request object.
     * @param response Response object.
     * @throws IOException the io exception
     */
    @RequestMapping(value = "/error")
    public void error(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Exception exception = validateMaxUploadSizeExceededException(request);
        if(exception != null){
            exceptionHandler.doResolveHandlerMethodException(request, response, null, exception);
        } else {
            response.sendRedirect(request.getContextPath() +
                    Constants.SLASH + Constants.HASH +
                    Constants.SLASH + Constants.ERROR +
                    Constants.SLASH);
        }
    }

    /**
     * Returns the path of the error page.
     *<
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * Validate multi part exception exception.
     *
     * @param request the request
     * @return the exception
     */
    private Exception validateMaxUploadSizeExceededException(HttpServletRequest request) {
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        String message = exception != null ? exception.getMessage() : "";
        if( message.contains(MULTIPART_MAX_UPLOAD_SIZE_CODE) ) {
            message = message.replace(MULTIPART_MAX_UPLOAD_SIZE_CODE, "");
            message = CharMatcher.inRange('0','9').retainFrom(message);
            return new MaxUploadSizeExceededException(message != null ? Long.valueOf(message):1L, exception);
        }
        return null;
    }
}