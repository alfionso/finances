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

package es.alfonsomarin.finances.core.validation;

import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class SizeFieldValidatorTest {

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private SizeFieldValidation validator;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        when(messageSource.getMessage(anyString(),anyObject(),anyObject())).thenReturn("Error");
        validator.setMessageSource(messageSource);
    }
    
    /**
     * On validate then error.
     */
    @Test
    public void onValidateThenError(){
        validator.setField("description");
        validator.setSize(2);
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("aaaa");
        ResultExecution resultExecution = validator.onValidate(metadataRecord);
        Assert.assertTrue("Is incorrect", StringUtils.hasText(resultExecution.getMessage()));
    }


    /**
     * On validate then ok.
     */
    @Test
    public void onValidateThenOk(){
        validator.setField("description");
        validator.setSize(10);
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("aaaa");
        ResultExecution resultExecution = validator.onValidate(metadataRecord);
        Assert.assertTrue("Is correct", !StringUtils.hasText(resultExecution.getMessage()));
    }
   
}
