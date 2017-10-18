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

import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.core.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Transaction Controller
 *
 * @author alfonso.marin.lopez
 */
@RestController
@CrossOrigin
@RequestMapping("/api/transaction")
@Api(value = "/transaction", description = "Transaction Resource API")
public class TransactionController {
    
    private TransactionService transactionService;

    /**
     * Create transaction.
     * https://stackoverflow.com/questions/21329426/spring-mvc-multipart-request-with-json
     *
     * @param transaction the transaction
     * @return the transaction
     * @throws Exception the exception
     */
    @ApiOperation(value = "Create a new transaction from a metadata file", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = Constants.RESPONSE_CREATED),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = Constants.RESPONSE_BAD_REQUEST),
            @ApiResponse(code = HttpServletResponse.SC_CONFLICT, message = Constants.RESPONSE_CONFLICT)
    })
    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public Transaction create(@RequestBody @Valid Transaction transaction) {
        return transactionService.save(transaction);
    }

    /**
     * Update transaction.
     *
     * @param transactionId the transaction id
     * @param transaction   the transaction
     * @return the transaction
     * @throws Exception the exception
     */
    @ApiOperation(value = "Update an existing transaction", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = Constants.RESPONSE_CREATED),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = Constants.RESPONSE_BAD_REQUEST),
            @ApiResponse(code = HttpServletResponse.SC_CONFLICT, message = Constants.RESPONSE_CONFLICT)
    })
    @RequestMapping(
            path = "/{transactionId}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public Transaction update(@PathVariable Long transactionId, @RequestBody @Valid Transaction transaction){
        return transactionService.save(transaction);
    }

    /**
     * Gets all transaction.
     *
     * @return the all transaction
     * @throws Exception the exception
     */
    @ApiOperation(value = "Find all transaction", response = Transaction.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_OK, message = Constants.RESPONSE_OK)
    })
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @ResponseStatus(value = HttpStatus.OK)
    public List<Transaction> getAllCollectRequest() {
        return transactionService.findAll();
    }

    /**
     * Delete transaction.
     *
     * @param idTransaction the id transaction
     */
    @ApiOperation(value = "Delete an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = Constants.RESPONSE_NO_CONTENT),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = Constants.RESPONSE_NOT_FOUND)
    })
    @RequestMapping(
            path = "/{idTransaction}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCollectRequest(@PathVariable Long idTransaction){
        transactionService.delete(idTransaction);
    }

    /**
     * Sets transaction service.
     *
     * @param transactionService the transaction service
     */
    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
