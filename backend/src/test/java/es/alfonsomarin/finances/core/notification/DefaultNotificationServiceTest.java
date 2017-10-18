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

package es.alfonsomarin.finances.core.notification;

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.domain.notification.Notification;
import es.alfonsomarin.finances.core.template.TemplateService;
import es.alfonsomarin.finances.core.exception.TemplateProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultNotificationServiceTest {
    @Mock
    private JavaMailSenderImpl mockJavaMailSender;

    @Mock
    private TemplateService mockTemplateService;

    @Mock
    private MessageSource mockMessageSource;

    @InjectMocks
    private DefaultNotificationService defaultNotificationService;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        defaultNotificationService.setJavaMailSender(mockJavaMailSender);
        defaultNotificationService.setMessageSource(mockMessageSource);
        defaultNotificationService.setTemplateService(mockTemplateService);
        ReflectionTestUtils.setField(defaultNotificationService, "appUrl", "/");
        ReflectionTestUtils.setField(defaultNotificationService, "contextPath", "finances");
        ReflectionTestUtils.setField(defaultNotificationService, "mailServerHost", "localhost");
        ReflectionTestUtils.setField(defaultNotificationService, "mailServerPort", TestConstants.NOTIFICATION_MAIL_PORT);
        ReflectionTestUtils.setField(defaultNotificationService, "port", "");
        ReflectionTestUtils.setField(defaultNotificationService, "defaultSender", TestConstants.NOTIFICATION_MAIL_SENDER);
        when(mockMessageSource.getMessage(anyString(), anyObject(), any(Locale.class))).thenReturn(TestConstants.NOTIFICATION_MESSAGE);
    }

    /**
     * Send notification.
     *
     * @throws TemplateProcessingException the template processing exception
     */
    @Test
    public void sendNotification() throws TemplateProcessingException {
        Notification notification = NotificationStubFactory.generateNotificationMessage();

        when(mockJavaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        when(mockTemplateService.process(anyString(), anyObject())).thenReturn(new byte []{});

        doNothing().when(mockJavaMailSender).send(any(MimeMessage.class));

        defaultNotificationService.sendNotification(notification);

        verify(mockJavaMailSender, times(1)).send(any(MimeMessage.class));

        assertNotNull(mockJavaMailSender);
    }

    /**
     * Send notification throws messaging exception.
     *
     * @throws TemplateProcessingException the template processing exception
     */
    @Test
    public void sendNotificationThrowsMessagingException() throws TemplateProcessingException, MessagingException {
        Notification notification = NotificationStubFactory.generateNotificationMessage();

        when(mockJavaMailSender.createMimeMessage()).thenThrow(MessagingException.class);

        when(mockTemplateService.process(anyString(), anyObject())).thenThrow(MessagingException.class);

        defaultNotificationService.sendNotification(notification);
        verify(mockJavaMailSender, times(1)).createMimeMessage();
    }

    /**
     * Send notification throws template processing exception.
     *
     * @throws TemplateProcessingException the template processing exception
     */
    @Test
    public void sendNotificationThrowsTemplateProcessingException() throws TemplateProcessingException {
        Notification notification = NotificationStubFactory.generateNotificationMessage();

        when(mockJavaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        when(mockTemplateService.process(anyString(), anyObject())).thenReturn(new byte []{});

        doThrow(TemplateProcessingException.class).when(mockJavaMailSender).send(any(MimeMessage.class));

        defaultNotificationService.sendNotification(notification);

        verify(mockJavaMailSender, times(1)).send(any(MimeMessage.class));
    }
}