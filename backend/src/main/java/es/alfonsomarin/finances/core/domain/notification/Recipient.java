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

package es.alfonsomarin.finances.core.domain.notification;


 /**
  * The type Recipient.
  *
  * @author alfonso.marin.lopez
  */
 public class Recipient {

    private String username;
    private String fullName;
    private String email;

     /**
      * Gets username.
      *
      * @return the username
      */
     public String getUsername() {
        return username;
    }

     /**
      * Sets username.
      *
      * @param username the username
      */
     public void setUsername(String username) {
        this.username = username;
    }

     /**
      * Gets full name.
      *
      * @return the full name
      */
     public String getFullName() {
        return fullName;
    }

     /**
      * Sets full name.
      *
      * @param fullName the full name
      */
     public void setFullName(String fullName) {
        this.fullName = fullName;
    }

     /**
      * Gets email.
      *
      * @return the email
      */
     public String getEmail() {
        return email;
    }

     /**
      * Sets email.
      *
      * @param email the email
      */
     public void setEmail(String email) {
        this.email = email;
    }

     /**
      * Username recipient.
      *
      * @param username the username
      * @return the recipient
      */
     public Recipient username(String username) {
        this.username = username;
        return this;
    }

     /**
      * Full name recipient.
      *
      * @param fullName the full name
      * @return the recipient
      */
     public Recipient fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

     /**
      * Email recipient.
      *
      * @param email the email
      * @return the recipient
      */
     public Recipient email(String email) {
        this.email = email;
        return this;
    }
}
