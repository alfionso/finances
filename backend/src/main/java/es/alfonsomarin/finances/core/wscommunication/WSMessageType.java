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
 * The enum Ws message type.
 *
 * @author alfonso.marin.lopez
 */
public enum WSMessageType {
    /**
     * Transaction update ws message type.
     */
    TRANSACTION_UPDATE,
    /**
     * Transaction remove ws message type.
     */
    TRANSACTION_REMOVE,
    /**
     * Insert update ws message type.
     */
    INSERT_UPDATE,
    /**
     * Insert remove ws message type.
     */
    INSERT_REMOVE
}
