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
import es.alfonsomarin.finances.core.exception.ApplicationException;
import es.alfonsomarin.finances.core.wscommunication.WSCommunicationService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(PowerMockRunner.class)
public class DefaultTransactionServiceTest {

    /* Mocks */
    private WSCommunicationService mockWsCommunicationService = Mockito.mock(WSCommunicationService.class);
    private TransactionRepository mockTransactionRepository = Mockito.mock(TransactionRepository.class);
    private TransactionConvert mockTransactionConverter = Mockito.mock(TransactionConvert.class);

    /* private */
    private TransactionConvert transactionConvert = new TransactionConvert();
    private Transaction transaction = TransactionStubFactory.generateTransaction();
    private TransactionEntity transactionEntity = TransactionStubFactory.generateTransactionEntity();
    private DefaultTransactionService transactionService = new DefaultTransactionService();

    /**
     * The Expected exception.
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Setup.
     */
    @Before
    public void setup(){
        transactionService.setTransactionConvert(transactionConvert);
        transactionService.setTransactionRepository(mockTransactionRepository);
        transactionService.setWsCommunicationService(mockWsCommunicationService);
    }

    /**
     * Create transaction return transaction.
     */
    @Test
    public void createTransactionReturnTransaction() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        when(mockTransactionRepository.save((TransactionEntity)anyObject())).thenReturn(transactionEntity);
        transaction.setId(null);
        Transaction returnTransaction = transactionService.save(transaction);
        assertNotNull(returnTransaction.getId());
        assertEquals(TestConstants.ASSERT_EQUALS, transaction.getDescription(), returnTransaction.getDescription());
        assertEquals(sdf.format(transaction.getDateCreation()), sdf.format(returnTransaction.getDateCreation()));
    }

    /**
     * Save transaction return transaction request with Exception.
     */
    @Test(expected = ApplicationException.class)
    public void saveTransactionReturnApplicationException(){
        transactionEntity.setDescription("descrition 1");
        transaction.setDescription("Description 2");
        when(mockTransactionRepository.save((TransactionEntity)anyObject())).thenReturn(transactionEntity);
        assertNotNull(transactionService.save(transaction));
    }
    

    /**
     * Get All transactions.
     */
    @Test
    public void getAllTransactionsOK(){
        List<TransactionEntity> transactionEntityList = new ArrayList<>();

        when(mockTransactionRepository.findAll()).thenReturn(transactionEntityList);
        List<Transaction> expectedTransactionList = transactionService.findAll();
        assertEquals(expectedTransactionList.size(), transactionEntityList.size());
    }

    /**
     * Get transaction by Id.
     */
    @Test
    public void getTransactionById(){
        try {
            when(mockTransactionRepository.findOne(anyLong())).thenReturn(transactionEntity);
            Transaction expectedTransaction = transactionService.get(transactionEntity.getId());

            assertSame(expectedTransaction.getId() , transactionEntity.getId());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Delete transaction.
     */
    @Test
    public void deleteTransactionOK(){
        try {
            when(mockTransactionRepository.findOne(anyLong())).thenReturn(transactionEntity);
            when(mockTransactionConverter.toCore(any(TransactionEntity.class))).thenReturn(transaction);

            doNothing().when(mockTransactionRepository).delete(transaction.getId());

            transactionService.delete(transaction.getId());
        } catch (Exception e) {
            fail();
        }
    }

    
    
}
