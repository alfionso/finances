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
package es.alfonsomarin.finances.core.domain.user;

import es.alfonsomarin.finances.core.util.Constants;
import es.alfonsomarin.finances.core.validation.constraints.Email;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static java.util.Optional.ofNullable;

/**
 * A User that has access to the system
 *
 * @author alfonso.marin.lopez
 */
public class User {

    /**
     * The user identifier.
     */
    private Long id;

    /**
     * The username, used to login to the application.
     */
    @NotBlank(message = MessageCodes.VALIDATION_MANDATORY)
    @Size(message = MessageCodes.VALIDATION_MANDATORY_LESSER_THAN, max = Constants.MAX_USERNAME)
    private String username;

    /**
     * The password assigned to the user.
     */
    @NotBlank(message = MessageCodes.VALIDATION_MANDATORY)
    @Size(message = MessageCodes.VALIDATION_MANDATORY_LESSER_THAN, max = Constants.MAX_STRING)
    private String password;

    /**
     * The application role of the user, it defines what permissions the user has to
     * perform actions in the system.
     */
    @NotNull(message = MessageCodes.VALIDATION_MANDATORY)
    private Set<String> roles;

    /**
     */
    @NotBlank(message = MessageCodes.VALIDATION_MANDATORY)
    @Size(message = MessageCodes.VALIDATION_MANDATORY_LESSER_THAN, max = Constants.MAX_STRING)
    private String fullName;

    /**
     * The e-mail of the user for communication.
     */
    @NotBlank(message = MessageCodes.VALIDATION_MANDATORY)
    @Size(message = MessageCodes.VALIDATION_MANDATORY_LESSER_THAN, max = Constants.MAX_STRING)
    @Email(message = MessageCodes.VALIDATION_WRONG_EMAIL)
    private String email;

    /**
     * A flag indicating if the system should sendToAll e-mail notifications related
     * to changes on any of the forms from the user IP Office.
     */
    private Boolean notificationEnabled;

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
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets roles.
     *
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Sets roles.
     *
     * @param roles the roles
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
     * Is notification enabled boolean.
     *
     * @return the boolean
     */
    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    /**
     * Sets notification enabled.
     *
     * @param notificationEnabled the notification enabled
     */
    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    /**
     * Fluent API
     *
     * @param id the id
     * @return the user
     */
    public User withId(Long id) {
        setId(id);
        return this;
    }

    /**
     * Username user.
     *
     * @param username the username
     * @return the user
     */
    public User username(String username) {
        setUsername(username);
        return this;
    }

    /**
     * Password user.
     *
     * @param password the password
     * @return the user
     */
    public User password(String password) {
        setPassword(password);
        return this;
    }

    /**
     * Roles user.
     *
     * @param roles the roles
     * @return the user
     */
    public User roles(Set<String> roles) {
        setRoles(roles);
        return this;
    }

    /**
     * Full name user.
     *
     * @param fullName the full name
     * @return the user
     */
    public User fullName(String fullName) {
        setFullName(fullName);
        return this;
    }

    /**
     * Email user.
     *
     * @param email the email
     * @return the user
     */
    public User email(String email) {
        setEmail(email);
        return this;
    }

    /**
     * Notification enabled user.
     *
     * @param notificationEnabled the notification enabled
     * @return the user
     */
    public User notificationEnabled(boolean notificationEnabled) {
        setNotificationEnabled(notificationEnabled);
        return this;
    }

    @Override
    public String toString() {
        return "username=" + ofNullable(username).orElse(Constants.ANONYMOUS);
    }
}