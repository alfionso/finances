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

package es.alfonsomarin.finances.core.security.authentication.internal;

import es.alfonsomarin.finances.core.util.ReflectionUtils;
import es.alfonsomarin.finances.core.exception.DefaultExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The custom {@code InternalAuthenticationFailureHandler} invokes
 * the application's error handling logic to provide proper
 * logout feedback to API clients
 *
 * @author alfonso.marin.lopez
 */
@Profile("internal")
@Component
public class InternalAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String METHOD_NAME = "onAuthenticationFailure";

    private DefaultExceptionHandler defaultExceptionHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        defaultExceptionHandler.resolveException(
                request, response, getHandlerMethod(), exception
        );
    }

    private HandlerMethod getHandlerMethod() {

        Class[] arguments = new Class[] {
                HttpServletRequest.class, HttpServletResponse.class, Authentication.class
        };
        return new HandlerMethod(
                this, ReflectionUtils.getMethod(this.getClass(), METHOD_NAME, arguments)
        );
    }

    /**
     * Sets default exception handler.
     *
     * @param defaultExceptionHandler the default exception handler
     */
    @Autowired
    public void setDefaultExceptionHandler(DefaultExceptionHandler defaultExceptionHandler) {
        this.defaultExceptionHandler = defaultExceptionHandler;
    }
}
