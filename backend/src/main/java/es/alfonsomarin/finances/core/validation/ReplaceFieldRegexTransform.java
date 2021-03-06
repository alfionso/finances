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
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Clean the field with a regex, removing the match from the field.
 *
 * @author alfonso.marin.lopez
 */
public class ReplaceFieldRegexTransform extends AbstractMetadataExecutor {
    
    private static final Logger LOGGER = LogManager.getLogger(ReplaceFieldRegexTransform.class);
    private String regex;
    private String field;
    private String replacement;

    @Override
    public ResultExecution onValidate(MetadataRecord record) {
        LOGGER.debug("Clean with Regex the field");
        ResultExecution resultExecution = new ResultExecution();
        Optional<Object> val = Optional.empty();
        try {
            val = ReflectionUtils.getFieldValueThroughtGetter(record, field);
        } catch (IntrospectionException|IllegalAccessException|InvocationTargetException e) {
            LOGGER.warn("The field ["+field+"] not exist in Metadata record");
        }
        if(val.isPresent() && val.get() instanceof String){
            String cleanField = ((String) val.get()).replaceAll(regex, replacement);
                resultExecution.setField(field);
                resultExecution.setNewValue(cleanField);
        }
        return resultExecution;
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
     * Sets regex.
     *
     * @param regex the regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * Sets replacement.
     *
     * @param replacement the replacement
     */
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }
}
