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

import es.alfonsomarin.finances.core.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Configuration controller
 *
 * @author alfonso.marin.lopez
 */
@RestController
@RequestMapping("/api/config")
@Api(value = "/config", description = "Application Configuration resources API (Master Data, Localisation, etc.)")
public class ConfigurationController {
    
    private ConfigurationService configurationService;

    /**
     * Gets labels.
     *
     * @return Full list of labels
     */
    @ApiOperation(value = "Get the localised list of messages and labels for the current (or best matching) locale")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK, response = Properties.class)
    })
    @RequestMapping(
            path = "/messages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @ResponseBody Properties getLabels() {
        return configurationService.getAllProperties(LocaleContextHolder.getLocale());
    }

    /**
     * Set configuration service.
     *
     * @param configurationService the configuration service
     */
    @Autowired
    public void setConfigurationService(ConfigurationService configurationService){
        this.configurationService = configurationService;
    }
    
    
}
