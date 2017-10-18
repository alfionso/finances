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
import org.springframework.context.MessageSource;

import java.util.Optional;

/**
 * The type Abstract metadata validator.
 *
 * @author alfonso.marin.lopez
 */
public abstract class AbstractMetadataExecutor implements MetadataExecutor {
    private boolean enabled = true;
    private String pathToFile;
    private MessageSource messageSource;

    @Override
    public Optional<ResultExecution> validate(MetadataRecord record, String pathToFile) {
        this.pathToFile = pathToFile;
        if(enabled)
            return Optional.ofNullable(onValidate(record));
        return Optional.empty();
    }

    /**
     * On validate result validation.
     *
     * @param record the record
     * @return the result validation
     */
    public abstract ResultExecution onValidate(MetadataRecord record);

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets path to file.
     *
     * @return the path to file
     */
    public String getPathToFile() {
        return pathToFile;
    }

    /**
     * Sets path to file.
     *
     * @param pathToFile the path to file
     */
    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    /**
     * Gets message source.
     *
     * @return the message source
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * Sets message source.
     *
     * @param messageSource the message source
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
