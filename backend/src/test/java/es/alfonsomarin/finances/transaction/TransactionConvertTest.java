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

import es.alfonsomarin.finances.TestConstants;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author alfonso.marin.lopez
 */
public class TransactionConvertTest {
    /**
     * The Collect request convert.
     */
    TransactionConvert collectRequestConvert = new TransactionConvert();

    /**
     * Test to core.
     */
    @Test
    public void testToCore(){
        TransactionEntity transactionEntity = TransactionStubFactory.generateTransactionEntity();
        Transaction transaction = collectRequestConvert.toCore(transactionEntity);
        assertsEqueals(transaction, transactionEntity);
    }

    /**
     * Test to persistence.
     */
    @Test
    public void testToPersistence(){
        Transaction transaction = TransactionStubFactory.generateTransaction();
        TransactionEntity transactionEntity = collectRequestConvert.toPersistence(transaction);
        assertsEqueals(transaction, transactionEntity);
    }
    
    private void assertsEqueals(Transaction transaction, TransactionEntity transactionEntity){
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getId(), transactionEntity.getId());
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getCategory(), transactionEntity.getCategory());
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getDescription(), transactionEntity.getDescription());
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getType(), transactionEntity.getType());
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getValue(), transactionEntity.getValue());
    }
    
}
