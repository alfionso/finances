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

import es.alfonsomarin.finances.core.security.authorization.WebSecurityCorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionManagementFilter;

/**
 * Develop web security config
 * Only to work with IDE execution and the frontend with webpack server
 *
 * @author alfonso.marin.lopez
 */
@Profile("dev")
@Configuration
@EnableWebSecurity
public class DevelopWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";
    private static final String API_URL = "/api/**";
    private static final String API_MESSAGES_URL = "/api/config/messages";
    private static final String REPORTS_URL = "/report/**";
    
    @Value("${system.admin.user}")
    private String userName;
    
    @Value("${system.admin.password}")
    private String password;

    private AuthenticationSuccessHandler authenticationSuccessHandler;

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionManagementFilter sessionManagementFilter;

    private LogoutSuccessHandler logoutSuccessHandler;

    private LogoutHandler cookieClearingLogoutHandler;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // only for develop, the post, put and remove http methods require it
        http
                .addFilter(authenticationFilter())
                .addFilter(sessionManagementFilter)
                .addFilterBefore(new WebSecurityCorsFilter(), ChannelProcessingFilter.class)
                .headers()
                    .cacheControl().and()
                    .xssProtection().and()
                    .frameOptions().sameOrigin().and()
                .authorizeRequests()
                    .antMatchers(API_MESSAGES_URL).permitAll()
                    .antMatchers(API_URL).permitAll()
                    .antMatchers(REPORTS_URL).permitAll()
                    .and()
                .logout()
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .addLogoutHandler(cookieClearingLogoutHandler)
                    .invalidateHttpSession(true);
    }

    /**
     * Configure global.
     *
     * @param auth the auth
     * @throws Exception the exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser(userName).password(password).roles("USER");
    }

    /**
     * Password encoder b crypt password encoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return filter;
    }

    /**
     * Sets authentication success handler.
     *
     * @param authenticationSuccessHandler the authentication success handler
     */
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    /**
     * Sets authentication failure handler.
     *
     * @param authenticationFailureHandler the authentication failure handler
     */
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    /**
     * Sets session management filter.
     *
     * @param sessionManagementFilter the session management filter
     */
    @Autowired
    public void setSessionManagementFilter(SessionManagementFilter sessionManagementFilter) {
        this.sessionManagementFilter = sessionManagementFilter;
    }

    /**
     * Sets logout success handler.
     *
     * @param logoutSuccessHandler the logout success handler
     */
    @Autowired
    public void setLogoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
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
