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

package es.alfonsomarin.finances.core.template;

import es.alfonsomarin.finances.core.exception.TemplateProcessingException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>The template service declares a method to process a template
 * and generate a byte array output.</p>
 *
 * @author alfonso.marin.lopez
 */
@Transactional
public interface TemplateService {

    /**
     * Processes a template with entity data.
     *
     * @param templateName Template name.
     * @param entitiesMap  Entities to replace placeholders in templates.
     * @return Byte array of the template with data interpolated.
     * @throws TemplateProcessingException the template processing exception
     */
    byte[] process(String templateName, Map<String, Object> entitiesMap) throws TemplateProcessingException;
    
}
