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

import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.exception.TemplateProcessingException;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * {@code FreeMarkerTemplateService} is an implementation of the {@code TemplateService}
 * that replaces a given template's placeholders by given input data. For that end,
 * it uses FreeMarker.
 *
 * @author alfonso.marin.lopez
 */
@Service
class FreeMarkerTemplateService implements TemplateService, InitializingBean {

    
    private ConfigurationService configurationService;
    
    private Configuration configuration;
    
    /**
     * Initializes the FreeMarker API engine to benefit from template caching.
     *
     * @throws IOException if templates folder cannot be found.
     */
    @Override
    public void afterPropertiesSet() throws IOException {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setTemplateLoader(
                new FileTemplateLoader(
                        configurationService.getTemplatesDirectory().toFile()
                )
        );
    }

    /**
     * Processes a template with entity data using FreeMarker.
     *
     * @param templateName Template name.
     * @param entitiesMap  Entities to replace placeholders in templates.
     * @return Processed template.
     */
    @Override
    public byte[] process(String templateName, Map<String, Object> entitiesMap) throws TemplateProcessingException {

        Writer processedTemplate = new StringWriter();

        try {
            configuration
                    .getTemplate(templateName)
                    .process(entitiesMap, processedTemplate);
            processedTemplate.flush();
        } catch (IOException | TemplateException ex) {
            throw new TemplateProcessingException(ex);
        }

        return processedTemplate.toString().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Sets configuration service.
     *
     * @param configurationService the configuration service
     */
    @Autowired
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
