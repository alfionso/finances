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

/**
 * The type Configuration cache.
 *
 * @author alfonso.marin.lopez
 */
class ConfigurationCache {

    /**
     * Message source
     */
    private CustomReloadableResourceBundleMessageSource messageBundle;

    /**
     * Gets message bundle.
     *
     * @return the message bundle
     */
    public CustomReloadableResourceBundleMessageSource getMessageBundle() {
        return messageBundle;
    }

    /**
     * Sets message bundle.
     *
     * @param messageBundle the message bundle
     */
    public void setMessageBundle(CustomReloadableResourceBundleMessageSource messageBundle) {
        this.messageBundle = messageBundle;
    }

}
