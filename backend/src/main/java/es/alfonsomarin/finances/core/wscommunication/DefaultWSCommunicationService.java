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

import es.alfonsomarin.finances.core.domain.transaction.Transaction;
import es.alfonsomarin.finances.core.domain.insert.InsertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Default ws communication service.
 *
 * @author alfonso.marin.lopez
 */
@Service
public class DefaultWSCommunicationService implements WSCommunicationService{
    
    private WSCommunicationHandler wsCommunicationHandler;
    
    @Override
    public void sendToAll(WSMessage message) {
        wsCommunicationHandler.sendToAll(message);
    }
    

    @Override
    public void sendToAllTransaction(Transaction transaction) {
        WSMessage wsMessage = new WSMessage()
                .id(transaction.getId())
                .entity(transaction)
                .type(WSMessageType.TRANSACTION_UPDATE);
        wsCommunicationHandler.sendToAll(wsMessage);
    }

    @Override
    public void sendToAllInsert(InsertRequest insertRequest) {
        WSMessage wsMessage = new WSMessage()
                .id(insertRequest.getId())
                .entity(insertRequest)
                .type(WSMessageType.INSERT_UPDATE);
        wsCommunicationHandler.sendToAll(wsMessage);
    }

    /**
     * Sets ws communication handler.
     *
     * @param wsCommunicationHandler the ws communication handler
     */
    @Autowired
    public void setWsCommunicationHandler(WSCommunicationHandler wsCommunicationHandler) {
        this.wsCommunicationHandler = wsCommunicationHandler;
    }
}
