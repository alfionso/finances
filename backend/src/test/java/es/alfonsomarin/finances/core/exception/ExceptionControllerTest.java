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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionControllerTest {

    @InjectMocks
    private ExceptionController exceptionController;

    @Mock
    private DefaultExceptionHandler mockExceptionHandler;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private Exception mockException;

    private final String MULTIPART_MAX_UPLOAD_SIZE_MESSAGE = "java.io.IOException: UT000054: The maximum size 3145728 for an individual file in a multipart request was exceeded";

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        Mockito.reset(mockRequest, mockResponse);
        exceptionController = new ExceptionController(mockExceptionHandler);
    }

    /**
     * Error send redirect.
     *
     * @throws Exception the exception
     */
    @Test
    public void errorSendRedirect() throws Exception {
        when(mockRequest.getAttribute(anyString())).thenReturn(mockException);
        when(mockException.getMessage()).thenReturn("");
        when(mockResponse.getStatus()).thenReturn(anyInt());

        exceptionController.error(mockRequest, mockResponse);

        verify(mockRequest, times(1)).getAttribute(anyString());
        verify(mockRequest, times(1)).getContextPath();

        verify(mockResponse, times(1)).sendRedirect(anyString());

        assertNotNull(mockException);
    }

    /**
     * Error upload size exceeded redirect.
     *
     * @throws Exception the exception
     */
    @Test
    public void errorUploadSizeExceeded() throws Exception {
        when(mockRequest.getAttribute(anyString())).thenReturn(mockException);
        when(mockException.getMessage()).thenReturn(MULTIPART_MAX_UPLOAD_SIZE_MESSAGE);
        when(mockExceptionHandler.doResolveHandlerMethodException(eq(mockRequest), eq(mockResponse), any(HandlerMethod.class), any(MaxUploadSizeExceededException.class))).thenReturn(new ModelAndView());

        exceptionController.error(mockRequest, mockResponse);

        verify(mockRequest, times(1)).getAttribute(anyString());

        assertNotNull(mockExceptionHandler);
    }

    /**
     * Gets error path return path.
     */
    @Test
    public void getErrorPathReturnPath() {
        assertTrue("/error".equals(exceptionController.getErrorPath()));
    }
}
