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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

/**
 * Metadata item reader
 *
 * @author alfonso.marin.lopez
 */
public class InsertItemReader extends FlatFileItemReader<MetadataRecord> {

    /**
     * Instantiates a new Metadata item reader.
     *
     * @param pathToFile the path to file
     * @param skipLines the skip header
     */
    public InsertItemReader(String pathToFile, int skipLines, String delimiter){
        this.setResource(new FileSystemResource(pathToFile));
        this.setLineMapper(new DefaultLineMapper<MetadataRecord>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {{
                    setNames(new String[]{"date",
                            "description",
                            "value"});
                    setStrict(false); // avoid error with more columns
                    setDelimiter(delimiter);
                }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<MetadataRecord>() {{
                    setTargetType(MetadataRecord.class);
                }});
            }
        });
        this.setLinesToSkip(skipLines);
    }
}
