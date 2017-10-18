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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Base validation config.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@ConfigurationProperties //Without label, but this annotation es mandatory for work
public class BaseValidationConfig {
    
    private MessageSource messageSource;
    private EmptyFieldValidation emptyFieldValidator;
    private List<RegexValidation> regexValidator;
    private List<MatchInListValidation> matchInListValidator;
    private List<SizeFieldValidation> sizeFieldValidator;
    private List<ReplaceFieldRegexTransform> replaceFieldRegexTransform;


    /**
     * Gets empty field validator.
     *
     * @return the empty field validator
     */
    public EmptyFieldValidation getEmptyFieldValidator() {
        return emptyFieldValidator;
    }

    /**
     * Sets empty field validator.
     *
     * @param emptyFieldValidator the empty field validator
     */
    public void setEmptyFieldValidator(EmptyFieldValidation emptyFieldValidator) {
        this.emptyFieldValidator = emptyFieldValidator;
    }

    /**
     * Gets regex validator.
     *
     * @return the regex validator
     */
    public List<RegexValidation> getRegexValidator() {
        return regexValidator;
    }

    /**
     * Sets regex validator.
     *
     * @param regexValidator the regex validator
     */
    public void setRegexValidator(List<RegexValidation> regexValidator) {
        this.regexValidator = regexValidator;
    }

    /**
     * Gets match in list validator.
     *
     * @return the match in list validator
     */
    public List<MatchInListValidation> getMatchInListValidator() {
        return matchInListValidator;
    }

    /**
     * Sets match in list validator.
     *
     * @param matchInListValidator the match in list validator
     */
    public void setMatchInListValidator(List<MatchInListValidation> matchInListValidator) {
        this.matchInListValidator = matchInListValidator;
    }

    /**
     * Gets size field validator.
     *
     * @return the size field validator
     */
    public List<SizeFieldValidation> getSizeFieldValidator() {
        return sizeFieldValidator;
    }

    /**
     * Sets size field validator.
     *
     * @param sizeFieldValidator the size field validator
     */
    public void setSizeFieldValidator(List<SizeFieldValidation> sizeFieldValidator) {
        this.sizeFieldValidator = sizeFieldValidator;
    }

    /**
     * Gets clean field regex validator.
     *
     * @return the clean field regex validator
     */
    public List<ReplaceFieldRegexTransform> getReplaceFieldRegexTransform() {
        return replaceFieldRegexTransform;
    }

    /**
     * Sets clean field regex validator.
     *
     * @param replaceFieldRegexTransform the clean field regex validator
     */
    public void setReplaceFieldRegexTransform(List<ReplaceFieldRegexTransform> replaceFieldRegexTransform) {
        this.replaceFieldRegexTransform = replaceFieldRegexTransform;
    }

    /**
     * Get list validators list.
     *
     * @return the list
     */
    public List<MetadataExecutor> getListValidators(){
        
        List<MetadataExecutor> list = new ArrayList<>();
        
        Optional.ofNullable(emptyFieldValidator).ifPresent(validator -> {
            validator.setMessageSource(messageSource);
            list.add(validator);
        });
        Optional.ofNullable(regexValidator).ifPresent(validators -> {
            validators.stream().forEach(validator -> {
                validator.setMessageSource(messageSource);
                list.add(validator);
            });
        });
        
        Optional.ofNullable(matchInListValidator).ifPresent(validators -> { 
            validators.stream().forEach(validator -> {
                validator.setMessageSource(messageSource);
                list.add(validator);
            });
        });
        Optional.ofNullable(sizeFieldValidator).ifPresent(validators -> {
            validators.stream().forEach(validator -> {
                validator.setMessageSource(messageSource);
                list.add(validator);
            });
        });

        Optional.ofNullable(replaceFieldRegexTransform).ifPresent(validators -> {
            validators.stream().forEach(validator -> {
                validator.setMessageSource(messageSource);
                list.add(validator);
            });
        });
        
        return list;
    }


    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
}
