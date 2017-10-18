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

package es.alfonsomarin.finances.core.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * <p>The {@code ExceptionResolverFactory} returns an instance of the adequate
 * {@code ExceptionResolver} based on the exception that needs to be resolved.</p>
 * <p>
 * <p>Validation exceptions require a specialized treatment whereas controlled
 * exceptions and the remaining non-controlled exceptions can be dealt with in
 * a more standard fashion.</p>
 *
 * @author alfonso.marin.lopez
 * @see ExceptionResolver
 */
@Component
class ExceptionResolverFactory {

    private ExceptionResolver applicationExceptionResolver;
    
    private ExceptionResolver defaultExceptionResolver;

    /**
     * In-memory mapping of exception resolvers.
     */
    private Map<String, ExceptionResolver> resolverMappings = new HashMap<>();

    /**
     * Initialize mapping.
     */
    @PostConstruct
    public void init() {

        // APPLICATION
        resolverMappings.put(ApplicationException.class.getName(), applicationExceptionResolver);
        
    }

    /**
     * Returns an instance of the adequate exception resolver,
     * either validation, application or default.
     *
     * @param exceptionName class name of the exception
     * @return instance of specialized {@code ExceptionResolver}
     */
    ExceptionResolver getExceptionResolverInstance(String exceptionName) {
        return ofNullable(resolverMappings.get(exceptionName))
                .orElse(defaultExceptionResolver);
    }

    /**
     * Sets application exception resolver.
     *
     * @param applicationExceptionResolver the application exception resolver
     */
    @Autowired
    @Qualifier("applicationExceptionResolver")
    public void setApplicationExceptionResolver(ExceptionResolver applicationExceptionResolver) {
        this.applicationExceptionResolver = applicationExceptionResolver;
    }

    /**
     * Sets default exception resolver.
     *
     * @param defaultExceptionResolver the default exception resolver
     */
    @Autowired
    @Qualifier("defaultExceptionResolver")
    public void setDefaultExceptionResolver(ExceptionResolver defaultExceptionResolver) {
        this.defaultExceptionResolver = defaultExceptionResolver;
    }
}
