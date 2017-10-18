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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Insert validation config.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@ConfigurationProperties(prefix="validations")
public class InsertValidationConfig extends BaseValidationConfig {

    /**
     * Insert validator list list.
     *
     * @return the list
     */
    @Bean
    public List<MetadataExecutor> insertValidatorList(){
        return getListValidators();
    }
    
    
}
