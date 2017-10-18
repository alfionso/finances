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

package es.alfonsomarin.finances.core.security;

import es.alfonsomarin.finances.core.security.authentication.CookieClearingLogoutHandler;
import es.alfonsomarin.finances.core.security.authorization.CsrfCookieFilter;
import es.alfonsomarin.finances.core.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionManagementFilter;

/**
 * Configuration class for security aspects.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer {
    @Value("${app.delete.cookies.names}")
    private String appDeleteCookiesNames;

    private InvalidSessionStrategy invalidSessionStrategy;

    /**
     * Csrf token repository csrf token repository.
     *
     * @return the csrf token repository
     */
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

    /**
     * Register CSRF Cookie filter bean.
     *
     * @return The filter registration bean.
     */
    @Bean
    public CsrfCookieFilter csrfCookieFilter() {
        return new CsrfCookieFilter();
    }

    /**
     * Session management filter session management filter.
     *
     * @return the session management filter
     */
    @Bean
    public SessionManagementFilter sessionManagementFilter() {
        SessionManagementFilter filter = new SessionManagementFilter(
                new HttpSessionSecurityContextRepository()
        );
        filter.setInvalidSessionStrategy(invalidSessionStrategy);
        return filter;
    }

    /**
     * Cookie clearing logout handler logout handler.
     *
     * @return the logout handler
     */
    @Bean
    public LogoutHandler cookieClearingLogoutHandler() {
        return new CookieClearingLogoutHandler(appDeleteCookiesNames.split(Constants.COMMA));
    }

    /**
     * Sets invalid session strategy.
     *
     * @param invalidSessionStrategy the invalid session strategy
     */
    @Autowired
    public void setInvalidSessionStrategy(InvalidSessionStrategy invalidSessionStrategy) {
        this.invalidSessionStrategy = invalidSessionStrategy;
    }
}