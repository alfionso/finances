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
public class RegexValidatorTest {
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private RegexValidation regexValidator;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        when(messageSource.getMessage(anyString(),anyObject(),anyObject())).thenReturn("Error");
        regexValidator.setMessageSource(messageSource);
    }
    
    /**
     * On validate not exist field then nothing.
     */
    @Test
    public void onValidateNotExistFieldThenNothing(){
        regexValidator.setField("XXXXX");
        regexValidator.setRegex("[a-z]*");
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("aaaa");
        ResultExecution resultExecution = regexValidator.onValidate(metadataRecord);
        Assert.assertTrue("Is incorrect", StringUtils.hasText(resultExecution.getMessage()));
    }

    /**
     * On validate not text field then nothing.
     */
    @Test
    public void onValidateNotTextFieldThenNothing(){
        regexValidator.setField("description");
        regexValidator.setRegex("[a-z]*");
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("aaaa");
        ResultExecution resultExecution = regexValidator.onValidate(metadataRecord);
        Assert.assertFalse("The description is incorrect and match with regex", StringUtils.hasText(resultExecution.getMessage()));
    }

    /**
     * On validate not correct field then message.
     */
    @Test
    public void onValidateNotCorrectFieldThenMessage(){
        regexValidator.setField("description");
        regexValidator.setRegex("[a-z]*");
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("1111");
        ResultExecution resultExecution = regexValidator.onValidate(metadataRecord);
        Assert.assertTrue("The description is not correct", StringUtils.hasText(resultExecution.getMessage()));
    }

    /**
     * On validate correct field then message.
     */
    @Test
    public void onValidateCorrectFieldThenMessage(){
        regexValidator.setField("description");
        regexValidator.setRegex("[a-z]*");
        MetadataRecord metadataRecord = new MetadataRecord()
                .description("abc");
        ResultExecution resultExecution = regexValidator.onValidate(metadataRecord);
        Assert.assertTrue("The description is correct", !StringUtils.hasText(resultExecution.getMessage()));
    }
}
