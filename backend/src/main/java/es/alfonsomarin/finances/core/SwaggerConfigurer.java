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

package es.alfonsomarin.finances.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * To know if swagger is working, access to http://localhost/finances/v2/api-docs
 *
 * @author alfonso.marin.lopez
 */
@Profile("dev")
@Configuration
@EnableSwagger2
public class SwaggerConfigurer extends WebMvcConfigurerAdapter {

    /**
     * Swagger properties property sources placeholder configurer.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    @Primary
    public static PropertySourcesPlaceholderConfigurer swaggerProperties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/static/api-doc").setViewName("forward:/api-doc/index.html");
        registry.addViewController("/static/api-doc/").setViewName("forward:/api-doc/index.html");
        super.addViewControllers(registry);
    }

    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.directModelSubstitute(Date.class, LocalDateTime.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("es.alfonsomarin.finances"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Finances REST API")
                .description("Back-end REST API providing access to Finances features.")
                .contact("Alfonso")
                .version("1.0.0")
                .build();
    }
}
