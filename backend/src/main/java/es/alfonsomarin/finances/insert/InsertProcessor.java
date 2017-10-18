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

package es.alfonsomarin.finances.insert;

import es.alfonsomarin.finances.core.domain.metadata.MetadataRecord;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.ReflectionUtils;
import es.alfonsomarin.finances.core.validation.MetadataExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * The type Insert processor.
 *
 * @author alfonso.marin.lopez
 */
public class InsertProcessor implements ItemProcessor<MetadataRecord,InsertResult>{

    private static final Logger LOGGER = LogManager.getLogger(InsertProcessor.class);
    
    private List<MetadataExecutor> validatorList;
    
    private List<MetadataExecutor> transformList;
    
    private String pathToFile;

    /**
     * Instantiates a new Insert processor.
     *
     * @param pathToFile the path to file
     */
    public InsertProcessor(String pathToFile){
        this.pathToFile = pathToFile;
    }

    @Override
    public InsertResult process(MetadataRecord metadataRecord) throws Exception {
        InsertResult insertResult = new InsertResult();
        LOGGER.debug("Validating file");
        metadataRecord = validate(metadataRecord);
        if(!StringUtils.hasText(metadataRecord.getError())){
            LOGGER.debug("transform file");
            metadataRecord = transform(metadataRecord);
            insertResult.setInserted(true);
            insertResult.setErrorMessage(null);
        }else{
            insertResult.setErrorMessage(metadataRecord.getError());
            insertResult.setInserted(false);
        }
        insertResult.setMetadataRecord(metadataRecord);
        //TimeUnit.SECONDS.sleep(1);
        return insertResult;
    }

    private MetadataRecord validate(MetadataRecord metadataRecord){
        StringBuilder errorlist = new StringBuilder();
        validatorList.stream().forEach(
                validator ->  validator.validate(metadataRecord, pathToFile).ifPresent(
                        result ->{
                            boolean changed = StringUtils.hasText(result.getMessage());
                            if(changed){
                                if(errorlist.length()>0){
                                    errorlist.append(Constants.HYPHEN);
                                }
                                errorlist.append(result.getMessage());
                            }
                        }));
        metadataRecord.setError(errorlist.toString());
        return metadataRecord;
    }

    private MetadataRecord transform(MetadataRecord metadataRecord){
        transformList.stream().forEach(
                transform ->  transform.validate(metadataRecord, pathToFile).ifPresent(
                        result ->{
                            if(StringUtils.hasText(result.getNewValue())){
                                try {
                                    ReflectionUtils.setFieldValueThroughtGetter(metadataRecord,result.getField(),result.getNewValue());
                                } catch (InvocationTargetException|IllegalAccessException|IntrospectionException e) {
                                    LOGGER.warn(e);
                                } 
                            }
                        }));
        return metadataRecord;
    }

   

    /**
     * Sets validator list.
     *
     * @param validatorList the validator list
     */
    @Resource(name= "insertValidatorList")  
    public void setValidatorList(List<MetadataExecutor> validatorList) {
        this.validatorList = validatorList;
    }

    /**
     * Sets transform list.
     *
     * @param transformList the transform list
     */
    @Resource(name= "insertTransformList")
    public void setTransformList(List<MetadataExecutor> transformList) {
        this.transformList = transformList;
    }
}
