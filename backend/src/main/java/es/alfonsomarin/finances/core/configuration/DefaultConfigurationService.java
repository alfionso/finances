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
package es.alfonsomarin.finances.core.configuration;

import es.alfonsomarin.finances.core.domain.notification.Recipient;
import es.alfonsomarin.finances.core.domain.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Default configuration service
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultConfigurationService implements ConfigurationService {
    private static final Logger LOGGER = LogManager.getLogger(DefaultConfigurationService.class);

    private ConfigurationCache configurationCache;
    
    @Value("${spring.config.location:./config/}")
    private String springConfigLocation;
    
    @Value("${system.admin.user}")
    private String defaultUsername;
    
    @Value("${system.list.emails}")
    private String defaultUserMail;
    
    private User currentUser;

    /**
     * The Templates dir.
     */
    static final String TEMPLATES_DIR = "templates";

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        configurationCache = new ConfigurationCache();
        configurationCache.setMessageBundle(new CustomReloadableResourceBundleMessageSource());
        currentUser = new User()
                .email(defaultUserMail)
                .username(defaultUsername);
    }

    public CustomReloadableResourceBundleMessageSource getMessageBundle() {
        return configurationCache.getMessageBundle();
    }

    @Override
    public String getProperty(String key) {
        return configurationCache.getMessageBundle().getMessage(key,null,LocaleContextHolder.getLocale());
    }

    @Override
    public Path getTemplatesDirectory() {
        return Paths.get(springConfigLocation,TEMPLATES_DIR);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public List<Recipient> getListMailsRecipients() {
        List<Recipient> result = new ArrayList<>();
        try{
            String[] array = this.defaultUserMail.split(",");
            for(String mail:array){
                result.add(new Recipient()
                        .email(mail)
                        .username(currentUser.getUsername()));
            }
        }catch(Exception e){
            LOGGER.warn(e);
        }
        return result;
    }

    @Override
    public Properties getAllProperties(Locale locale) {
        return configurationCache.getMessageBundle().getAllProperties(LocaleContextHolder.getLocale());
        
    }
    
}
