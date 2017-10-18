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

package es.alfonsomarin.finances.core.wscommunication;

import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import es.alfonsomarin.finances.core.domain.insert.InsertRequestStatus;
import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.transaction.TransactionStubFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author alfonso.marin.lopez
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultWSCommunicationServiceTest {

    @Mock
    private WebSocketSession mockWebSocket;
    
    @InjectMocks
    private WSCommunicationHandler wsCommunicationHandler;
  
    @InjectMocks
    private DefaultWSCommunicationService defaultWSCommunicationService;
    
    private Transaction transaction = TransactionStubFactory.generateTransaction();

    @Before
    public void setUp() throws Exception {
        when(mockWebSocket.getId()).thenReturn("1");
        when(mockWebSocket.isOpen()).thenReturn(Boolean.TRUE);
        wsCommunicationHandler.afterConnectionEstablished(mockWebSocket);
        defaultWSCommunicationService.setWsCommunicationHandler(wsCommunicationHandler);
    }
    
    @After
    public void tearDown() throws Exception {
        wsCommunicationHandler.afterConnectionClosed(mockWebSocket, CloseStatus.NORMAL);
    }

    @Test
    public void sendToAllOK() throws Exception{
        WSMessage message = new WSMessage();
        defaultWSCommunicationService.sendToAll(message);

        verify(mockWebSocket, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    public void sendToAllCollect() throws Exception{
        defaultWSCommunicationService.sendToAllTransaction(transaction);

        verify(mockWebSocket, times(1)).sendMessage(any(TextMessage.class));
    }

    @Test
    public void sendToAllInsert() throws Exception{
        InsertRequest insertRequest = new InsertRequest();
        insertRequest.setId(1L);
        insertRequest.setStatus(InsertRequestStatus.NOT_STARTED);

        defaultWSCommunicationService.sendToAllInsert(insertRequest);

        verify(mockWebSocket, times(1)).sendMessage(any(TextMessage.class));
        
    }

}
