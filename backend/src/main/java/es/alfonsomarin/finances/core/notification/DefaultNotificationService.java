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

import es.alfonsomarin.finances.core.domain.notification.Notification;
import es.alfonsomarin.finances.core.template.TemplateService;
import es.alfonsomarin.finances.core.domain.notification.Recipient;
import es.alfonsomarin.finances.core.exception.TemplateProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Service for sending notifications
 *
 * @author alfonso.marin.lopez
 */
@Service
class DefaultNotificationService implements NotificationService {

    private static final String MESSAGE_SOURCE_KEY = "messageSource";
    private static final String LOCALE_KEY = "locale";
    private static final String ADDRESS_KEY = "address";
    private static final String MESSAGE_BODY_KEY = "messageBodyKey";

    private static final String LOGO_FILE = "alfonsomarin.png";
    private static final String LOGO_ID = "logoAlfonsoMarin";

    @Value("${spring.mail.default.sender}")
    private String defaultSender;

    @Value("${server.app-url}")
    private String appUrl;

    @Value("${server.context-path:finances/}")
    private String contextPath;

    @Value("${spring.mail.host}")
    private String mailServerHost;

    @Value("${spring.mail.port}")
    private int mailServerPort;
    
    @Value("${server.port}")
    private String port;
    
    private JavaMailSender javaMailSender;

    private TemplateService templateService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private MessageSource messageSource;

    /**
     * Send notification (e-mail) to corresponding recipients.
     *
     * @param notification Notification to be sent.
     */
    @Override
    @Async
    public void sendNotification(Notification notification) {
        notification.getRecipients()
                .forEach(recipient -> send(notification, recipient));
    }

    @Async
    private void send(Notification notification, Recipient recipient) {

        try {
            ((JavaMailSenderImpl)javaMailSender).setPort(mailServerPort);
            ((JavaMailSenderImpl)javaMailSender).setHost(mailServerHost);
            javaMailSender.send(prepareMessage(notification, recipient));
        } catch (MessagingException | TemplateProcessingException e) {
            logger.error("Error sending notification message: {}", e.getMessage(), e);
        }


    }

    private MimeMessage prepareMessage(Notification notification, Recipient recipient) throws MessagingException, TemplateProcessingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(new InternetAddress(defaultSender));
        helper.setTo(new InternetAddress(recipient.getEmail()));
        helper.setSubject(messageSource.getMessage(notification.getType().getSubject(), null, Locale.ENGLISH));
        helper.setText(generateBody(notification), true);
        //helper.addInline(LOGO_ID, configurationService.getImage(LOGO_FILE));

        return message;
    }

    private String generateBody(Notification notification) throws TemplateProcessingException {
        Map<String, Object> context = new HashMap<>();
        context.put(notification.getEntityName(), notification.getData());
        context.put(MESSAGE_SOURCE_KEY, messageSource);
        context.put(LOCALE_KEY, Locale.ENGLISH);
        context.put(ADDRESS_KEY, appUrl.concat(":").concat(port).concat(contextPath).concat("/"));
        return new String(
                templateService.process(notification.getType().getTemplate(), context),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Sets java mail sender.
     *
     * @param javaMailSender the java mail sender
     */
    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sets template service.
     *
     * @param templateService the template service
     */
    @Autowired
    @Qualifier("freeMarkerTemplateService")
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }
}
