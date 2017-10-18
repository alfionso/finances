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
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static es.alfonsomarin.finances.core.util.Constants.DOT;

/**
 * The type Size field validator.
 *
 * @author alfonso.marin.lopez
 */
public class SizeFieldValidation extends AbstractMetadataExecutor {
    
    private static final Logger LOGGER = LogManager.getLogger(SizeFieldValidation.class);
    private String field;
    private int size;
    
    @Override
    public ResultExecution onValidate(MetadataRecord record) {
        LOGGER.debug("Size maximum validation");
        ResultExecution resultExecution = new ResultExecution();
            try {
                Optional<Object> value = ReflectionUtils.getFieldValueThroughtGetter(record, field);
                if(value.isPresent() && 
                        value.get() instanceof String &&
                        ((String)value.get()).length()>size){
                    resultExecution.setMessage(
                            getMessageSource().getMessage(
                                    Constants.MESSAGE.concat(DOT).concat(String.valueOf(MessageCodes.CODE_VALIDATION_STRING_SIZE_ERROR)),
                                    new Object[]{field,size}, LocaleContextHolder.getLocale())
                    );
                    resultExecution.setCode(MessageCodes.CODE_VALIDATION_STRING_SIZE_ERROR);
                }
            } catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
                resultExecution.setMessage(
                        getMessageSource().getMessage(
                                Constants.MESSAGE.concat(DOT).concat(String.valueOf(MessageCodes.CODE_VALIDATION_STRING_SIZE_ERROR)),
                                new Object[]{field}, LocaleContextHolder.getLocale())
                );
                resultExecution.setCode(MessageCodes.CODE_VALIDATION_FIELD_EMPTY);
            }
        
        return resultExecution;
    }

    /**
     * Gets field.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets field.
     *
     * @param field the field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }
}
