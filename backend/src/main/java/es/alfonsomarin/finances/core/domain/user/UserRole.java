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

import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import es.alfonsomarin.finances.core.util.Constants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * An application role that can be assigned to the User. It groups a set of
 * permissions, defining what actions the Users can perform in the system.
 *
 * @author alfonso.marin.lopez
 */
public class UserRole {

    /**
     * The role name, e.g. "Admin".
     */
    @NotBlank(message = MessageCodes.VALIDATION_MANDATORY)
    @Size(message = MessageCodes.VALIDATION_MANDATORY_LESSER_THAN, max = Constants.MAX_USERNAME)
    private String name;

    /**
     * The set of permissions assigned to this name.
     */
    @NotNull
    private Set<String> permissions;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Sets permissions.
     *
     * @param permissions the permissions
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * Fluent API
     *
     * @param name the name
     * @return the user role
     */
    public UserRole name(String name) {
        setName(name);
        return this;
    }

    /**
     * Permissions user role.
     *
     * @param permissions the permissions
     * @return the user role
     */
    public UserRole permissions(Set<String> permissions) {
        setPermissions(permissions);
        return this;
    }

}