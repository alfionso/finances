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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static es.alfonsomarin.finances.core.domain.common.exception.ExceptionType.SECURITY;
import static es.alfonsomarin.finances.core.domain.common.exception.MessageCodes.CODE_NOT_FOUND_USER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationExceptionResolverTest {

    @Mock
    private MessageSource mockMessageSource;

    @InjectMocks
    private ApplicationExceptionResolver applicationExceptionResolver;

    private ApplicationException applicationException = new ExceptionBuilder()
            .message(TestConstants.TEST_VALUE)
            .exceptionType(SECURITY)
            .code(CODE_NOT_FOUND_USER)
            .build();

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        applicationExceptionResolver.init();
        when(mockMessageSource.getMessage(anyString(), anyObject(), any(Locale.class))).thenReturn(TestConstants.TEST_VALUE);
    }

    /**
     * Resolve error response.
     */
    @Test
    public void resolveErrorResponse() {
        ExceptionDetail errorDetail = applicationExceptionResolver.resolveErrorResponse(applicationException);
        assertEquals(CODE_NOT_FOUND_USER, errorDetail.getCode());
    }

   
}
