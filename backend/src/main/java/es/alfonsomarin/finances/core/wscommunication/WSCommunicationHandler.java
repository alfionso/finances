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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Ws communication handler.
 *
 * @author alfonso.marin.lopez
 */
@Component
public class WSCommunicationHandler extends TextWebSocketHandler{
    private Map<String, WebSocketSession> sessionMap = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Send to all.
     *
     * @param message the message
     */
    public void sendToAll(WSMessage message){
        sessionMap.forEach((id, session) -> {
            if(session != null && session.isOpen()){
                try {
                    session.sendMessage(new TextMessage(encode(message)));
                } catch (IOException e) {
                    logger.error(e.getMessage().toString());
                }
            }
        });
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        if(!sessionMap.containsKey(session.getId())) {
            sessionMap.put(session.getId(), session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        if(sessionMap.containsKey(session.getId())){
            sessionMap.remove(session.getId());    
        }
        
    }
    
    private String encode(WSMessage message){
        String result;
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(message);
        }catch(IOException e){
            result = "ERROR ENCODING BO MESSAGE";
        }
        return result;
    }
}
