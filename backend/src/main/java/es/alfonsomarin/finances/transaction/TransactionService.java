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

import java.util.List;

/**
 * Collect service
 *
 * @author alfonso.marin.lopez
 */
public interface TransactionService {

    /**
     * Gets transaction.
     *
     * @param id the id
     * @return the transaction
     */
    Transaction get(Long id);
    
    /**
     * Create or update a transaction.
     *
     * @param transaction the transaction
     * @return transaction
     */
    Transaction save(Transaction transaction);

    /**
     * Find all transaction list.
     *
     * @return the list
     */
    List<Transaction> findAll();

    /**
     * Delete transaction.
     *
     * @param id the id
     */
    void delete(Long id);
    
}
