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

package es.alfonsomarin.finances.core.security.authorization;

import es.alfonsomarin.finances.core.util.SecurityUtils;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;

/**
 * Filter that sets the CSRF token to a response cookie. Only gets fired when client
 * make a request that ends with "csrf".
 *
 * @author alfonso.marin.lopez
 */
public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final String FILTER_APPLIED = "__spring_security_session_mgmt_filter_applied";

    private static final String CSRF_URL = "csrf";
    private static final String API = "/api/";

    //To send cookie throw http and https
    private boolean secure=false;

    /**
     * This method deals with client side requests for CSRF tokens and sets a cookie
     * with the token value to the response if necessary.
     *
     * @param request Request.
     * @param response Response.
     * @param filterChain Filter chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (addCsrfCookieIfNecessary(request, response)) {
            return;
        }

        if (!request.getServletPath().contains(API)) {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }

        filterChain.doFilter(request, response);
    }

    private boolean addCsrfCookieIfNecessary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().endsWith(CSRF_URL)) {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            if(csrfToken!=null){
                response.addCookie(SecurityUtils.getCsrfCookie(
                        request, csrfToken.getToken(), secure
                        )
                );
            }
            response.setStatus(SC_CREATED);
            return true;
        }
        return false;
    }
}
