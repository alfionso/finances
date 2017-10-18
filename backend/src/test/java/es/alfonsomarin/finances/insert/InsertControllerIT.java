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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import es.alfonsomarin.finances.TestDatabaseConfiguration;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestDatabaseConfiguration
public class InsertControllerIT {

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * Sets .
     */
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    /**
     * Gets all insert request return insert requests.
     *
     * @throws Exception the exception
     */
    @Test
    public void getAllInsertRequestReturnInsertRequests() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        get("/api/insert")
                )
                .andExpect(status().isOk())
                .andReturn();
        List<InsertRequest> insertRequestList =
                mapper.readValue(
                        result.getResponse().getContentAsString(),
                        TypeFactory.defaultInstance().constructCollectionType(List.class, InsertRequest.class)
                );
        assertTrue(insertRequestList.size()>0);
    }

    /**
     * Gets insert request return insert request.
     *
     * @throws Exception the exception
     */
    @Test
    public void getInsertRequestReturnInsertRequest() throws Exception {
        MvcResult result = getOneInsertRequest(1L)
                .andExpect(status().isOk())
                .andReturn();

        InsertRequest insertRequest = mapper.readValue(result.getResponse().getContentAsString(), InsertRequest.class);

        assertNotNull(insertRequest);
    }

    private ResultActions getOneInsertRequest(Long insertId) throws Exception {
        return mockMvc
                .perform(
                        get("/api/insert/" + insertId)
                                .with(csrf())
                );
    }

    /**
     * Update insert request insert request.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateInsertRequestOK() throws Exception {
        MvcResult resultGetOne = getOneInsertRequest(2L)
                .andExpect(status().isOk())
                .andReturn();

        //convert JSON to InsertRequest object
        InsertRequest resultInsertRequest = mapper.readValue(resultGetOne.getResponse().getContentAsString(), InsertRequest.class);

        resultInsertRequest.setTotalLines(resultInsertRequest.getTotalLines() + 1);
        updateInsertRequest(resultInsertRequest.getId(), resultInsertRequest);

        MvcResult resultGetUpdated = getOneInsertRequest(2L)
                .andExpect(status().isOk())
                .andReturn();

        InsertRequest insertRequestUpdated = mapper.readValue(resultGetUpdated.getResponse().getContentAsString(), InsertRequest.class);

        assertEquals(resultInsertRequest.getTotalLines(), insertRequestUpdated.getTotalLines());
    }

    //AUXILIARY METHOD TO UPDATE AN INSERT_UPDATE REQUEST
    private ResultActions updateInsertRequest(Long insertRequestId, InsertRequest insertRequest) throws Exception {

        return mockMvc
                .perform(
                        put("/api/insert/" + insertRequestId)
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(insertRequest))
                );
    }

    /**
     * Start process.
     *
     * @throws Exception the exception
     */
    @Test
    public void startInsertRequestProcessOK() {
        try {
            MvcResult resultGetOne = getOneInsertRequest(1L)
                    .andExpect(status().isOk())
                    .andReturn();

            //convert JSON to InsertRequest object
            InsertRequest insertRequest = mapper.readValue(resultGetOne.getResponse().getContentAsString(), InsertRequest.class);

            MvcResult resultProcess = mockMvc
                                        .perform(
                                                post("/api/insert/" + insertRequest.getId() + "/start")
                                                        .with(csrf())
                                                        .contentType(APPLICATION_JSON)
                                                        .content(mapper.writeValueAsString(insertRequest))
                                        ).andExpect(status().isOk())
                                        .andReturn();

            assertTrue(resultProcess.getResponse().getContentAsString().length() >= 0);
        } catch (Exception e) {
            fail();
        }
    }
}
