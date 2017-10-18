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

package es.alfonsomarin.finances;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Convenience annotation to inject test data scripts into
 * integration tests, after the schema files and ddl-auto is executed
 *
 * @author alfonso.marin.lopez
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Sql(
        scripts = {
                "classpath:sql/cleanDB.sql",
                "classpath:sql/data-Transaction.sql",
                "classpath:sql/data-InsertRequest.sql"
        },
        config = @SqlConfig(
                encoding = "utf8"
        )
)
public @interface TestDatabaseConfiguration {
}