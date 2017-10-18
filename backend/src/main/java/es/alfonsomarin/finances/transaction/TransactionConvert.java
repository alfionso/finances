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

import es.alfonsomarin.finances.core.domain.common.converter.Converter;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

/**
 * Collect request convert
 *
 * @author alfonso.marin.lopez
 */
@Component
public class TransactionConvert implements Converter<Transaction, TransactionEntity> {
    
    @Override
    public TransactionEntity toPersistence(Transaction transaction) {
        return ofNullable(transaction)
                .map(
                        p -> toPersistence(p, new TransactionEntity())
                )
                .orElse(null);
    }

    @Override
    public TransactionEntity toPersistence(Transaction transaction, TransactionEntity transactionEntity) {
        return transactionEntity.id(transaction.getId())
                .dateCreation(transaction.getDateCreation())
                .description(transaction.getDescription())
                .category(transaction.getCategory())
                .type(transaction.getType())
                .value(transaction.getValue());
    }

    @Override
    public Transaction toCore(TransactionEntity transactionEntity) {
        return ofNullable(transactionEntity)
                .map(p -> {
                            Transaction transaction = new Transaction()
                                    .id(transactionEntity.getId())
                                    .dateCreation(transactionEntity.getDateCreation())
                                    .description(transactionEntity.getDescription())
                                    .category(transactionEntity.getCategory())
                                    .type(transactionEntity.getType())
                                    .value(transactionEntity.getValue());
                            return transaction;
                        }
                )
                .orElse(null);
    }
}
