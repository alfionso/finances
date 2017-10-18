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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigurationControllerTest {

    @Mock
    private ConfigurationService mockConfigurationService;

    @InjectMocks
    private ConfigurationController configurationController;

    private Properties configurationPorperties = new Properties();

    /**
     * Sets up.
     */
    @Before
    public void setUp() {

    }

    @Test
    public void getLabelsPropertiesOK(){
        when(mockConfigurationService.getAllProperties(LocaleContextHolder.getLocale())).thenReturn(configurationPorperties);
        Properties expectedLabels = configurationController.getLabels();
        assertEquals(configurationPorperties, expectedLabels);
    }
}
