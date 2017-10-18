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
import es.alfonsomarin.finances.core.domain.notification.NotificationType;
import es.alfonsomarin.finances.core.domain.notification.Recipient;
import es.alfonsomarin.finances.insert.DefaultInsertService;
import es.alfonsomarin.finances.transaction.TransactionStubFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfonso.marin.lopez
 */
public class NotificationStubFactory {
    /**
     * Generate notification message.
     *
     * @return the notification object
     */
    public static Notification generateNotificationMessage(){
        return new Notification()
                .data(TransactionStubFactory.generateTransaction())
                .entityName(DefaultInsertService.KEY_INSERT)
                .recipients(addRecipients())
                .type(NotificationType.INSERT_FINISHED);
    }

    /**
     * Add recipients notification.
     *
     * @return the notification
     */
    private static List<Recipient> addRecipients() {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient()
                .username(TestConstants.NOTIFICATION_USER)
                .fullName(TestConstants.NOTIFICATION_USER)
                .email(TestConstants.NOTIFICATION_MAIL_USER)
        );
        return recipients;
    }
}
