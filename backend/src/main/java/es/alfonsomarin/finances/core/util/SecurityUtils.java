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

package es.alfonsomarin.finances.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Security utility methods
 *
 * @author alfonso.marin.lopez
 */
public final class SecurityUtils {

    private static final String XSRF_TOKEN = "XSRF-TOKEN";
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * Retrieves current user from session context
     *
     * @return security user ({@code SecurityUser})
     */
    public static User getSecurityUser() {
        Optional<Authentication> authentication = ofNullable(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
        if (authentication.isPresent() && authentication.get().getPrincipal() instanceof User) {
            return (User) authentication.get().getPrincipal();
        }
        return new User(
                Constants.ANONYMOUS, Constants.ANONYMOUS,
                AuthorityUtils.createAuthorityList(ROLE_ANONYMOUS)
                        .stream().collect(Collectors.toSet()));
    }

    /**
     * Retrieves current user's username
     *
     * @return security user ({@code SecurityUser})
     */
    public static String getSecurityUsername() {
        return getSecurityUser().getUsername();
    }

    /**
     * Returns a CSRF token as a cookie.
     *
     * @param request Request.
     * @param token   CSRF token.
     * @param secure  the secure
     * @return CSRF cookie
     */
    public static Cookie getCsrfCookie(HttpServletRequest request, String token, boolean secure) {
        Cookie cookie = new Cookie(XSRF_TOKEN, token);
        cookie.setPath(request.getContextPath());
        cookie.setSecure(secure);
        return cookie;
    }

    
}
