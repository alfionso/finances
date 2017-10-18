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

package es.alfonsomarin.finances.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import es.alfonsomarin.finances.TestDatabaseConfiguration;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestDatabaseConfiguration
public class TransactionControllerIT {
    //Required to Generate JSON content from Java objects
    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;
    private GreenMail greenMail;

    private Transaction transaction;

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * Sets .
     */
    @Before
    public void setUp() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    /**
     * Stop mail server.
     */
    @After
    public void stopMailServer() {
        greenMail.stop();
    }

    //AUXILIARY METHOD TO CREATE AN TRANSACTION REQUEST
    private ResultActions createCollectRequest(Transaction transaction) throws Exception {

        return mockMvc
                .perform(
                        post("/api/transaction")
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(transaction))
                );
    }

    //AUXILIARY METHOD TO GET ALL TRANSACTION REQUEST
    private ResultActions searchAllCollectRequests() throws Exception {
        return mockMvc
                .perform(
                        get("/api/transaction")
                                .with(csrf())
                );
    }

    //AUXILIAR METHOD TO GET FIRST TRANSACTION REQUEST
    private Transaction searchFirstCollectRequest() throws Exception{
        MvcResult result = searchAllCollectRequests()
                .andExpect(status().isOk())
                .andReturn();

        //convert JSON to transaction request list object
        List<Transaction> transactionList = mapper.readValue(result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Transaction.class));

        Optional<Transaction> collectRequestOptional = transactionList.stream()
                        //.filter(w -> w.getId().equals(entityType.toString()))
                        .findFirst();

        if (collectRequestOptional.isPresent()){
            return collectRequestOptional.get();
        }else{
            return null;
        }
    }

    //AUXILIAR METHOD TO GET ONE TRANSACTION REQUEST
    private Transaction searchOneCollectRequest(Long collectRequesId) throws Exception{
        MvcResult result = searchAllCollectRequests()
                .andExpect(status().isOk())
                .andReturn();

        //convert JSON to transaction request list object
        List<Transaction> transactionList = mapper.readValue(result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Transaction.class));

        Optional<Transaction> collectRequestOptional = transactionList.stream()
                .filter(w -> w.getId().equals(collectRequesId))
                .findFirst();

        if (collectRequestOptional.isPresent()){
            return collectRequestOptional.get();
        }else{
            return null;
        }
    }

    //AUXILIAR METHOD TO UPDATE TRANSACTION REQUEST
    private ResultActions updateCollectRequest(Long collectRequestId, Transaction transaction) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/api/transaction/" + collectRequestId)
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(transaction))
                );
    }

    //AUXILIAR METHOD TO START TRANSACTION REQUEST VALIDATION
    private ResultActions startCollectRequest(Long collectRequestId) throws Exception {
        return mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/api/transaction/" + collectRequestId + "/validation")
                                .with(csrf())
                );
    }

    /**
     * Gets all transaction request return transaction requests.
     *
     * @throws Exception the exception
     */
    @Test
    public void getAllCollectRequestReturnCollectRequests() throws Exception {
        MvcResult result = searchAllCollectRequests()
                .andExpect(status().isOk())
                .andReturn();

        //convert JSON to agreements list object
        List<Transaction> transactionList = mapper.readValue(result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Transaction.class));

        assertTrue(transactionList.size()>0);
    }

    /**
     * Create transaction request return transaction requests.
     *
     * @throws Exception the exception
     */
    @Test
    public void createCollectRequestReturnCollectRequests() throws Exception {
        transaction = TransactionStubFactory.generateTransaction();

        createCollectRequest(transaction);

        MvcResult result = searchAllCollectRequests()
                .andExpect(status().isOk())
                .andReturn();

        //convert JSON to transaction request list object
        List<Transaction> transactionList = mapper.readValue(result.getResponse().getContentAsString(),
                TypeFactory.defaultInstance().constructCollectionType(List.class, Transaction.class));

        assertTrue(transactionList.size() > 0);
    }

   

    /**
     * Delete transaction request return transaction requests.
     *
     * @throws Exception the exception
     */
    @Test
    public void deleteCollectRequestOK() throws Exception {
        Transaction transaction = searchFirstCollectRequest();

        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/transaction/" + transaction.getId())
                                .with(csrf())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(transaction))
                ).andExpect(status().isNoContent())
                .andReturn();

        Transaction expectedResult = searchOneCollectRequest(transaction.getId());

        assertNull(expectedResult);
    }

}
