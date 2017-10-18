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

/**
 * The type Ws message.
 *
 * @author alfonso.marin.lopez
 */
public class WSMessage {
    
    
    private Object entity;
    private Long id;
    private WSMessageType type;


    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public WSMessageType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(WSMessageType type) {
        this.type = type;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * Sets entity.
     *
     * @param entity the entity
     */
    public void setEntity(Object entity) {
        this.entity = entity;
    }

    /**
     * Id ws message.
     *
     * @param id the id
     * @return the ws message
     */
    public WSMessage id(final Long id) {
        this.id = id;
        return this;
    }

    /**
     * Type ws message.
     *
     * @param type the type
     * @return the ws message
     */
    public WSMessage type(final WSMessageType type) {
        this.type = type;
        return this;
    }

    /**
     * Entity ws message.
     *
     * @param entity the entity
     * @return the ws message
     */
    public WSMessage entity(final Object entity) {
        this.entity = entity;
        return this;
    }


}
