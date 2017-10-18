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
package es.alfonsomarin.finances.core.configuration;

import es.alfonsomarin.finances.core.domain.notification.Recipient;
import es.alfonsomarin.finances.core.domain.user.User;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Configuration service interface
 *
 * @author alfonso.marin.lopez
 */
public interface ConfigurationService {
    /**
     * Retrieves properties for a given locale
     *
     * @param locale locale to get properties
     * @return properties from that given locale
     */
    Properties getAllProperties(Locale locale);

    /**
     * Gets message bundle.
     *
     * @return message bundle resource used
     */
    CustomReloadableResourceBundleMessageSource getMessageBundle();

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
    String getProperty(String key);

    /**
     * Gets templates directory.
     *
     * @return the templates directory
     */
    Path getTemplatesDirectory();

    /**
     * Gets current user.
     *
     * @return the current user
     */
    User getCurrentUser();

    /**
     * Gets list mails recipients.
     *
     * @return the list mails recipients
     */
    List<Recipient> getListMailsRecipients();
}
