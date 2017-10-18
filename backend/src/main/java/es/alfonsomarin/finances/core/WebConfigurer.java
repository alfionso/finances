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

import es.alfonsomarin.finances.core.configuration.ConfigurationService;
import es.alfonsomarin.finances.core.configuration.CustomReloadableResourceBundleMessageSource;
import es.alfonsomarin.finances.core.exception.DefaultExceptionHandler;
import es.alfonsomarin.finances.core.validation.BaseValidationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.util.UrlPathHelper;

import java.util.Locale;

/**
 * The type Web configurer.
 *
 * @author alfonso.marin.lopez
 */
@Configuration
@EnableCaching
@EnableAutoConfiguration
@EnableConfigurationProperties(BaseValidationConfig.class)
class WebConfigurer extends WebMvcConfigurerAdapter {
    
    private static final String MESSAGES_BASE_NAME = "i18n/messages";
    private static final String MESSAGES_DEFAULT_ENCODING = "UTF-8";
    private static final String MESSAGES_DEFAULT_LOCALE = "en";
    private static final String FILE = "file:";
    
    @Value("${spring.config.location:./config/}")
    private String springConfigLocation;
    
    private final ConfigurationService configurationService;

    /**
     * Instantiates a new Web configurer.
     *
     * @param configurationService the configuration service
     */
    @Autowired
    public WebConfigurer(ConfigurationService configurationService){
        this.configurationService = configurationService;
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/hello").setViewName("hello");
        super.addViewControllers(registry);
    }
    
   

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }


    /**
     * Locale resolver locale resolver.
     *
     * @return the locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver slr = new CookieLocaleResolver();
        slr.setDefaultLocale(new Locale(MESSAGES_DEFAULT_LOCALE));
        return slr;
    }

    /**
     * Locale change interceptor locale change interceptor.
     *
     * @return the locale change interceptor
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /**
     * Message source custom reloadable resource bundle message source.
     *
     * @return the custom reloadable resource bundle message source
     */
    @Bean
    public CustomReloadableResourceBundleMessageSource messageSource() {
        //configurationService.getMessageBundle().setBasename("classpath:i18n/messages");
        configurationService.getMessageBundle().setBasename(FILE + springConfigLocation + MESSAGES_BASE_NAME);
        configurationService.getMessageBundle().setDefaultEncoding(MESSAGES_DEFAULT_ENCODING);
        return configurationService.getMessageBundle();
    }

    /**
     * Default exception handler default exception handler.
     *
     * @return the default exception handler
     */
    @Bean
    public DefaultExceptionHandler defaultExceptionHandler() {
        return new DefaultExceptionHandler();
    }
     

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/csrf").allowedOrigins("http://localhost:8070");
        registry.addMapping("/api/**").allowedOrigins("http://localhost:8070");
        registry.addMapping("/**").allowedOrigins("http://localhost:8070");
    }
}