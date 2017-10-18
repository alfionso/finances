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

import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static es.alfonsomarin.finances.core.util.Constants.DOT;

/**
 * The type Empty field validator.
 *
 * @author alfonso.marin.lopez
 */
public class EmptyFieldValidation extends AbstractMetadataExecutor {
    
    private static final Logger LOGGER = LogManager.getLogger(EmptyFieldValidation.class);
    private List<String> fields;
    
    @Override
    public ResultExecution onValidate(MetadataRecord record) {
        LOGGER.debug("Empty validation");
        ResultExecution resultExecution = new ResultExecution();
        StringBuilder resultMessage = new StringBuilder();
        fields.forEach(fieldName -> {
            try {
                Optional<Object> value = ReflectionUtils.getFieldValueThroughtGetter(record, fieldName);
                if(!value.isPresent()){
                    resultMessage.append(
                            getMessageSource().getMessage(
                                    Constants.MESSAGE.concat(DOT).concat(String.valueOf(MessageCodes.CODE_VALIDATION_FIELD_EMPTY)),
                                    new Object[]{fieldName}, LocaleContextHolder.getLocale())
                    );
                }
            } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
                resultMessage.append(
                        getMessageSource().getMessage(
                                Constants.MESSAGE.concat(DOT).concat(String.valueOf(MessageCodes.CODE_VALIDATION_FIELD_EMPTY)),
                                new Object[]{fieldName}, LocaleContextHolder.getLocale())
                );
            } 
        });
        
        if(resultMessage.length()>0){
            resultExecution.setMessage(resultMessage.toString());
            resultExecution.setCode(MessageCodes.CODE_VALIDATION_FIELD_EMPTY);
        }
        
        return resultExecution;
    }

    /**
     * Sets fields.
     *
     * @param fields the fields
     */
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * Gets fields.
     *
     * @return the fields
     */
    public List<String> getFields() {
        return fields;
    }
}
