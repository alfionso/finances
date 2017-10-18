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

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.util.PathsUtils;
import es.alfonsomarin.finances.report.ReportConfiguration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * The type Insert controller.
 *
 * @author alfonso.marin.lopez
 */
@RestController
@CrossOrigin
@RequestMapping("/api/insert")
@Api(value = "/insert", description = "Insert Resource API REMOVE")
public class InsertController {

    private static final Logger LOGGER = LogManager.getLogger(InsertController.class);
    
    private InsertService insertService;
    
    private PathsUtils pathUtils;
    
    private ReportConfiguration reportConfiguration;

    /**
     * Create insert request.
     * https://stackoverflow.com/questions/21329426/spring-mvc-multipart-request-with-json
     *
     * @param insertRequest the insert request
     * @return the new insert request
     * @throws Exception the exception
     */
    @ApiOperation(value = "Create a new insert request from a file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, response = InsertRequest.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = Constants.RESPONSE_CREATED),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = Constants.RESPONSE_BAD_REQUEST),
            @ApiResponse(code = HttpServletResponse.SC_CONFLICT, message = Constants.RESPONSE_CONFLICT)
    })
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public InsertRequest createInsertRequest(@RequestPart("insertRequest") InsertRequest insertRequest,
                                             @RequestPart("content") MultipartFile content) {
        insertRequest = insertService.saveInsertRequest(insertRequest);
        int totalLines = createFile(insertRequest.getId(), content);
        if(totalLines==0){
            insertRequest.setStatus(InsertRequestStatus.FAILED);
        }else{
            insertRequest.setTotalLines(totalLines);
        }
        insertRequest = insertService.updateInsertRequest(insertRequest);
        return insertRequest; 
    }


    /**
     * Gets all insert request.
     *
     * @return the all insert request
     * @throws Exception the exception
     */
    @ApiOperation(value = "Find all insert request", response = InsertRequest.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public List<InsertRequest> getAllInsertRequest() throws Exception {
        return insertService.findAllInsertRequest();
    }

    /**
     * Gets insert request.
     *
     * @param idRequest the id request
     * @return the insert request
     * @throws Exception the exception
     */
    @ApiOperation(value = "Get insert request", response = InsertRequest.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = Constants.RESPONSE_NOT_FOUND)
    })
    @RequestMapping(
            path = "/{idRequest}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public InsertRequest getInsertRequest(@PathVariable Long idRequest) throws Exception {
        return insertService.getInsertRequest(idRequest);
    }

    /**
     * Update insert request insert request.
     *
     * @param insertRequestId the insert request id
     * @param insertRequest   the insert request
     * @return the insert request
     * @throws Exception the exception
     */
    @ApiOperation(value = "Update an existing insert request", response = InsertRequest.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = Constants.RESPONSE_BAD_REQUEST),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = Constants.RESPONSE_NOT_FOUND)
    })
    @RequestMapping(
            path = "/{insertRequestId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public InsertRequest updateInsertRequest(@PathVariable Long insertRequestId, @RequestBody InsertRequest insertRequest){
        return insertService.saveInsertRequest(insertRequest);
    }

    /**
     * Start process.
     *
     * @param id the id
     * @throws Exception the exception
     */
    @ApiOperation(value = "Start batch process for a insert request")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            path = "/{id}/start",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public InsertRequest startProcess(@PathVariable Long id) throws Exception {
        return insertService.startInsertProcess(id);
    }

    /**
     * Pause process.
     *
     * @param id the id
     * @throws Exception the exception
     */
    @ApiOperation(value = "Pause batch process for a insert request")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            path = "/{id}/pause",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public InsertRequest pauseProcess(@PathVariable Long id) throws Exception {
        return insertService.pauseInsertProcess(id);
    }

    /**
     * Resume process.
     *
     * @param id the id
     * @throws Exception the exception
     */
    @ApiOperation(value = "Resume batch process for a insert request")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            path = "/{id}/resume",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public InsertRequest resumeProcess(@PathVariable Long id) throws Exception {
        return insertService.resumeInsertProcess(id);
    }

    /**
     * Abort process.
     *
     * @param id the id
     * @throws Exception the exception
     */
    @ApiOperation(value = "Abort batch process for a insert request")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            path = "/{id}/abort",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public InsertRequest abortProcess(@PathVariable Long id) throws Exception {
        return insertService.abortInsertProcess(id);
    }

    /**
     * Sets insert service.
     *
     * @param insertService the insert service
     */
    @Autowired
    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    @Autowired
    public void setPathUtils(PathsUtils pathUtils) {
        this.pathUtils = pathUtils;
    }

    @Autowired
    public void setReportConfiguration(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    private int createFile(Long id, MultipartFile content){
        int result = 0;
        LineNumberReader lnr = null;
        try {
            Path path = pathUtils.absolutePathInsert(id,content.getOriginalFilename());
            File file = new File(path.toString());
            file.getParentFile().mkdirs();
            Files.write(path, content.getBytes());
            lnr = new LineNumberReader(new FileReader(file));
            lnr.skip(Long.MAX_VALUE);
            result = lnr.getLineNumber() - reportConfiguration.getLinesHeader();
        } catch (IOException e) {
            LOGGER.warn(e);
        }finally {
            try {
                lnr.close();
            } catch (IOException e) {
                //nothing to do
            }
        }
        return result;
    }


}
