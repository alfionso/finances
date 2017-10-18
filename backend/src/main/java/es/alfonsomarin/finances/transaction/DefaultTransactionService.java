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

import es.alfonsomarin.finances.core.domain.common.exception.ExceptionType;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.core.exception.ExceptionBuilder;
import es.alfonsomarin.finances.core.wscommunication.WSCommunicationService;
import es.alfonsomarin.finances.core.wscommunication.WSMessage;
import es.alfonsomarin.finances.core.wscommunication.WSMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default transaction service
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultTransactionService implements TransactionService {
    
    private TransactionRepository transactionRepository;
    
    private TransactionConvert transactionConvert;
    
    private WSCommunicationService wsCommunicationService;


    /**
     * This constant identify the domain object.
     */
    public static final String KEY_TRANSACTION = "transaction";
    
    @Override
    public Transaction get(Long id) {
        return transactionConvert.toCore(transactionRepository.findOne(id));
    }

    @Override
    public Transaction save(Transaction transaction) {
        if(transaction.getId()!=null){
            getEntity(transaction.getId());
        }
        transaction = transactionConvert.toCore(
                transactionRepository.save(
                        transactionConvert.toPersistence(transaction)));
        wsCommunicationService.sendToAllTransaction(transaction);
        return transaction;
    }
    

    @Override
    public List<Transaction> findAll() {
        return transactionConvert.toCore(transactionRepository.findAll());
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = get(id);
        transactionRepository.delete(id);
        wsCommunicationService.sendToAll(new WSMessage()
                                        .id(id)
                                        .type(WSMessageType.TRANSACTION_REMOVE)
                                        .entity(transaction));
    }

    /**
     * Sets transaction repository.
     *
     * @param transactionRepository the transaction repository
     */
    @Autowired
    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Sets transaction convert.
     *
     * @param transactionConvert the transaction convert
     */
    @Autowired
    public void setTransactionConvert(TransactionConvert transactionConvert) {
        this.transactionConvert = transactionConvert;
    }

    /**
     * Set ws communication service.
     *
     * @param wsCommunicationService the ws communication service
     */
    @Autowired
    public void setWsCommunicationService(WSCommunicationService wsCommunicationService){
        this.wsCommunicationService = wsCommunicationService;
    }

    private TransactionEntity getEntity(Long id){
        TransactionEntity entity = transactionRepository.findOne(id);
        if(entity==null){
            throw new ExceptionBuilder()
                    .message("Transaction not exists.")
                    .exceptionType(ExceptionType.DATA_INTEGRITY)
                    .code(MessageCodes.CODE_NOT_FOUND_TRANSACTION)
                    .resources(id.toString())
                    .build();
        }
        return entity;

    }
}
