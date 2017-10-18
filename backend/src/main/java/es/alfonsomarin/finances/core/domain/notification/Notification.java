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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Notification.
 *
 * @author alfonso.marin.lopez
 */
public class Notification {

    private NotificationType type;
    private List<Recipient> recipients;

    //can be InsertRequest or CollectRequest
    private String entityName;
    private Object data;
    
    private Map<String, Object> extraData;


    /**
     * Instantiates a new Notification.
     */
    public Notification() {
        this.recipients = new ArrayList<>();
        this.extraData = new HashMap<>();
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(NotificationType type) {
        this.type = type;
    }

    /**
     * Gets recipients.
     *
     * @return the recipients
     */
    public List<Recipient> getRecipients() {
        return recipients;
    }

    /**
     * Sets recipients.
     *
     * @param recipients the recipients
     */
    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }


    /**
     * Gets entity name.
     *
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Sets entity name.
     *
     * @param entityName the entity name
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Gets extra data.
     *
     * @return the extra data
     */
    public Map<String, Object> getExtraData() {
        return extraData;
    }

    /**
     * Sets extra data.
     *
     * @param extraData the extra data
     */
    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }

    /**
     * Type notification.
     *
     * @param type the type
     * @return the notification
     */
    public Notification type(NotificationType type) {
        this.type = type;
        return this;
    }

    /**
     * Recipients notification.
     *
     * @param recipients the recipients
     * @return the notification
     */
    public Notification recipients(List<Recipient> recipients) {
        this.recipients = recipients;
        return this;
    }

    /**
     * Entity name notification.
     *
     * @param entityName the entity name
     * @return the notification
     */
    public Notification entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    /**
     * Data notification.
     *
     * @param data the data
     * @return the notification
     */
    public Notification data(Object data) {
        this.data = data;
        return this;
    }

    /**
     * Extra data notification.
     *
     * @param extraData the extra data
     * @return the notification
     */
    public Notification extraData(final Map<String, Object> extraData) {
        this.extraData = extraData;
        return this;
    }


}
