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

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.Properties;

/**
 * The type Custom reloadable resource bundle message source.
 *
 * @author alfonso.marin.lopez
 */
public class CustomReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

    /**
     * Gets all properties.
     *
     * @param locale locale for messages file
     * @return list of properties from messages file
     */
    Properties getAllProperties(Locale locale) {

        clearCacheIncludingAncestors();
        PropertiesHolder propertiesHolder = getMergedProperties(locale);

        return propertiesHolder.getProperties();
    }
}