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

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.exception.TemplateProcessingException;
import es.alfonsomarin.finances.insert.InsertRequestStubFactory;
import freemarker.template.utility.NullArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class FreeMarkerTemplateServiceTest {

    /**
     * The Mock configuration service.
     */
    @Mock
    private ConfigurationService mockConfigurationService;
    /**
     * The Template service. 
     * It's necessary init the service with the correct implementation. InjecMock doesn't know it. 
     */
    @InjectMocks
    private TemplateService templateService = new FreeMarkerTemplateService();
    /**
     * The Mock message source.
     */
    @Mock
    private MessageSource mockMessageSource;

    private InsertRequest insertRequest = InsertRequestStubFactory.generateInsertRequest();

    /**
     * Sets .
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        when(mockConfigurationService.getTemplatesDirectory()).thenReturn(Paths.get(TestConstants.FREEMARKER_TEMPLATE_FOLDER));
        ((FreeMarkerTemplateService) templateService).setConfigurationService(mockConfigurationService);
        ((FreeMarkerTemplateService) templateService).afterPropertiesSet();
    }

    /**
     * Process template for null template throw null argument exception.
     *
     * @throws Exception the exception
     */
    @Test(expected = NullArgumentException.class)
    public void processTemplateForNullTemplateThrowNullArgumentException() throws Exception {
        templateService.process(null, null);
    }

    /**
     * Process template for empty context throw template processing exception.
     *
     * @throws TemplateProcessingException the template processing exception
     */
    @Test(expected = TemplateProcessingException.class)
    public void processTemplateForEmptyContextThrowTemplateProcessingException() throws TemplateProcessingException {
        templateService.process(TestConstants.REEMARKER_SUCCESS_TEMPLATE_FILENAME, new HashMap<>());
    }

    /**
     * Process template for template processed ok.
     */
    @Test
    public void processTemplateForTemplateProcessedOk() {
        Map<String, Object> context = new HashMap<>();
        context.put("insert", insertRequest);
        context.put("message", mockMessageSource);
        context.put("locale", Locale.ENGLISH);
        context.put("address", "");

        String processedTemplate = "";
        try {
            processedTemplate = new String(templateService
                    .process(
                            TestConstants.REEMARKER_SUCCESS_TEMPLATE_FILENAME, context
                    )
            );

        } catch (Exception e) {
            fail();
        }

        assertTrue(processedTemplate.contains(""));
    }
}
