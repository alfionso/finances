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

package es.alfonsomarin.finances.core.security.authorization.internal;

import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.exception.DefaultExceptionHandler;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import es.alfonsomarin.finances.core.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * The custom {@code InternalInvalidSessionStrategy} invokes the application's error
 * handling logic to provide proper session expiration feedback to API clients and
 * clears cookies.
 *
 * @author alfonso.marin.lopez
 */
@Profile("internal")
@Component
public class InternalInvalidSessionStrategy implements InvalidSessionStrategy {

    private DefaultExceptionHandler defaultExceptionHandler;

    private LogoutHandler cookieClearingLogoutHandler;

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        cookieClearingLogoutHandler.logout(request, response, null);
        defaultExceptionHandler.resolveException(
                request, response, null,
                new ExceptionBuilder()
                        .message("Session expired.")
                        .exceptionType(ExceptionType.SESSION_EXPIRED)
                        .resources(SecurityUtils.getSecurityUsername())
                        .build()
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

    /**
     * Sets cookie clearing logout handler.
     *
     * @param cookieClearingLogoutHandler the cookie clearing logout handler
     */
    @Autowired
    public void setCookieClearingLogoutHandler(LogoutHandler cookieClearingLogoutHandler) {
        this.cookieClearingLogoutHandler = cookieClearingLogoutHandler;
    }
}
