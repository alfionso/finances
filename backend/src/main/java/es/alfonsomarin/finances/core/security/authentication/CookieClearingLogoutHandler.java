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

//@formatter:off
//@formatter:on

package es.alfonsomarin.finances.core.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static es.alfonsomarin.finances.core.util.Constants.SLASH;
import static org.apache.commons.lang3.StringUtils.lastIndexOf;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * The type Cookie with path clearing logout handler.
 * <p>
 * Deletes cookies with others path, the cookies name is composed by path + name.
 * For example a cookie with path value:
 * /path1/cookiename   -> path: /path1 and name: cookiename
 * /cookiename         -> path: / and name: cookiename
 * cookiename          -> path: request.getContextPath() and name: cookiename
 */
public class CookieClearingLogoutHandler implements LogoutHandler {

    /** The Cookies to clear. */
	private final List<String> cookiesToClear;

    /**
     * Instantiates a new Cookie with path clearing logout handler.
     *
     * @param cookiesToClear the cookies to clear
     */
    public CookieClearingLogoutHandler(String... cookiesToClear) {
		Assert.notNull(cookiesToClear, "List of cookies cannot be null");
		this.cookiesToClear = Arrays.asList(cookiesToClear);
	}

    /**
     * Clears the pre configured cookies from the session.
     *
     * @param request Request object.
     * @param response Response object.
     * @param authentication Authentication object.
     */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String defaultCookiePath = !StringUtils.hasLength(request.getContextPath()) ? SLASH : request.getContextPath();
		for (String cookieWithPath : cookiesToClear) {
			Cookie cookie = new Cookie(getCookieName(cookieWithPath), null);
			cookie.setPath(getCookiePath(cookieWithPath, defaultCookiePath));
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	private String getCookieName(String cookieWithPath) {
		int index = lastIndexOf(cookieWithPath, SLASH);
		return substring(cookieWithPath, index + 1);
	}

	private String getCookiePath(String cookieWithPath, String defaultCookiePath) {
		String path = defaultCookiePath;
		int index = lastIndexOf(cookieWithPath, SLASH);
		if (index == 0) {
			path = SLASH;
		} else if (index > 0) {
			path = substring(cookieWithPath, 0, index);
		}
		return path;
	}
}
