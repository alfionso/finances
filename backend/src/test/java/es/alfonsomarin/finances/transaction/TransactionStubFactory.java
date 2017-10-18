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
import es.alfonsomarin.finances.core.util.DateTimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author alfonso.marin.lopez
 */
public class TransactionStubFactory {
    /**
     * Generate transaction entity.
     *
     * @return the transaction entity
     */
    public static TransactionEntity generateTransactionEntity(){
        return new TransactionEntity()
                .dateCreation(new Date())
                .description("description")
                .id(1L)
                .dateCreation(DateTimeUtils.nowPlus(5L))
                .category("category")
                .type("type")
                .value(BigDecimal.TEN);
    }

    /**
     * Generate transaction.
     *
     * @return the transaction
     */
    public static Transaction generateTransaction(){
        return new Transaction()
                .id(1L)
                .description("description")
                .category("category")
                .dateCreation(DateTimeUtils.nowPlus(5L))
                .type("type")
                .value(BigDecimal.TEN);
    }

   

    /**
     * Generate transaction request transaction request list.
     *
     * @return the list
     */
    public static List<Transaction> generateCollectRequestList() {
        List<Transaction> retList = new ArrayList<>();
        retList.add(generateTransaction());
        return retList;
    }

    /**
     * Generate transaction request transaction request entity list.
     *
     * @return the list
     */
    public static List<TransactionEntity> generateCollectRequestEntityList() {
        List<TransactionEntity> retList = new ArrayList<>();
        retList.add(generateTransactionEntity());
        return retList;
    }
}
