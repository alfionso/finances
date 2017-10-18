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

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionDetail;
import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.domain.user.User;
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Collections;

import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_NOT_FOUND_USER;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ReflectionUtils.class)
public class DefaultExceptionHandlerTest {

    @Mock
    private ExceptionResolverFactory mockExceptionResolverFactory;

    @InjectMocks
    private DefaultExceptionHandler defaultExceptionHandler;

    @Mock
    private ExceptionResolver mockExceptionResolver;

    private ApplicationException applicationException = new ExceptionBuilder()
            .message(TestConstants.TEST_VALUE)
            .exceptionType(ExceptionType.RESOURCE_NOT_FOUND)
            .code(CODE_NOT_FOUND_USER)
            .build();

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        PowerMockito.mockStatic(ReflectionUtils.class);
        PowerMockito.when(ReflectionUtils.unwrapProxy(anyObject())).thenReturn(TestConstants.TEST_VALUE);

        Logger mockLogger = mock(Logger.class);
        ReflectionTestUtils.setField(defaultExceptionHandler,"logger", mockLogger);
        doNothing().when(mockLogger).info(anyString());
        doNothing().when(mockLogger).warn(anyString());
        doNothing().when(mockLogger).warn(anyString(),any(Exception.class));
        doNothing().when(mockLogger).error(anyString(),any(Exception.class));
    }
    

    /**
     * Do resolve handler method exception handler method null.
     */
    @Test
    public void doResolveHandlerMethodExceptionHandlerMethodNull() {
        ExceptionDetail exceptionDetail = new ExceptionDetail("",1,"", Collections.emptyMap());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        when(mockExceptionResolverFactory.getExceptionResolverInstance(anyString())).thenReturn(mockExceptionResolver);
        when(mockExceptionResolver.resolveErrorResponse(any(Exception.class))).thenReturn(exceptionDetail);
        when(mockExceptionResolver.toJsonResponse(any(ExceptionDetail.class))).thenReturn(TestConstants.TEST_VALUE);

        ModelAndView expected = defaultExceptionHandler.doResolveHandlerMethodException(mockHttpServletRequest, mockHttpServletResponse, null, applicationException);
        assertNotNull("ModelAndView not null", expected);
    }

    /**
     * Do resolve handler method exception io exception.
     */
    @Test
    public void doResolveHandlerMethodExceptionIOException() {
        ExceptionDetail exceptionDetail = new ExceptionDetail("", 1,"", Collections.emptyMap());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        when(mockExceptionResolverFactory.getExceptionResolverInstance(anyString())).thenReturn(mockExceptionResolver);
        when(mockExceptionResolver.resolveErrorResponse(any(Exception.class))).thenReturn(exceptionDetail);
        when(mockExceptionResolver.toJsonResponse(any(ExceptionDetail.class))).thenThrow(IOException.class);

        ModelAndView expected = defaultExceptionHandler.doResolveHandlerMethodException(mockHttpServletRequest, mockHttpServletResponse, null, applicationException);
        assertNotNull("ModelAndView not null", expected);
    }

    /**
     * Do resolve handler method exception type error.
     *
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void doResolveHandlerMethodExceptionTypeError() throws NoSuchMethodException {
        ExceptionDetail exceptionDetail = new ExceptionDetail("", 1,"ERROR", Collections.emptyMap());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        when(mockExceptionResolverFactory.getExceptionResolverInstance(anyString())).thenReturn(mockExceptionResolver);
        when(mockExceptionResolver.resolveErrorResponse(any(Exception.class))).thenReturn(exceptionDetail);
        when(mockExceptionResolver.toJsonResponse(any(ExceptionDetail.class))).thenReturn(TestConstants.TEST_VALUE);

        HandlerMethod handlerMethod = new HandlerMethod(new User(), "getId");

        ModelAndView expected = defaultExceptionHandler.doResolveHandlerMethodException(mockHttpServletRequest, mockHttpServletResponse, handlerMethod, applicationException);
        assertNotNull("ModelAndView not null", expected);
    }

    /**
     * Do resolve handler method exception type warning.
     *
     * @throws NoSuchMethodException the no such method exception
     */
    @Test
    public void doResolveHandlerMethodExceptionTypeWarning() throws NoSuchMethodException {
        ExceptionDetail exceptionDetail = new ExceptionDetail("",1,"WARNING", Collections.emptyMap());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        when(mockExceptionResolverFactory.getExceptionResolverInstance(anyString())).thenReturn(mockExceptionResolver);
        when(mockExceptionResolver.resolveErrorResponse(any(Exception.class))).thenReturn(exceptionDetail);
        when(mockExceptionResolver.toJsonResponse(any(ExceptionDetail.class))).thenReturn(TestConstants.TEST_VALUE);

        ModelAndView expected = defaultExceptionHandler.doResolveHandlerMethodException(mockHttpServletRequest, mockHttpServletResponse, null, applicationException);
        assertNotNull("ModelAndView not null", expected);
    }

}
