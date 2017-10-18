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

package es.alfonsomarin.finances.core.domain.notification;

/**
 * The enum Notification type.
 *
 * @author @author alfonso.marin.lopez
 */
public enum NotificationType {
    /**
     * Insert files finished body type.
     */
    INSERT_FINISHED("insert_finish_template.html", "notification.subject.insert.finish");

    private final String template;
    private final String subject;
    
    NotificationType(String template, String subject){
        this.subject = subject;
        this.template = template;
    }

    /**
     * Gets template.
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
}
