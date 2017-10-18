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

package es.alfonsomarin.finances.core.validation;

/**
 * The type Result validation.
 *
 * @author alfonso.marin.lopez
 */
public class ResultExecution {
    private int code;
    private String message;
    private String field;
    private String newValue;

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets field.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets field.
     *
     * @param field the field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets new value.
     *
     * @return the new value
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * Sets new value.
     *
     * @param newValue the new value
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * Code result validation.
     *
     * @param code the code
     * @return the result validation
     */
    public ResultExecution code(final int code) {
        this.code = code;
        return this;
    }

    /**
     * Message result validation.
     *
     * @param message the message
     * @return the result validation
     */
    public ResultExecution message(final String message) {
        this.message = message;
        return this;
    }

    /**
     * Field result execution.
     *
     * @param field the field
     * @return the result execution
     */
    public ResultExecution field(final String field) {
        this.field = field;
        return this;
    }

    /**
     * New value result execution.
     *
     * @param newValue the new value
     * @return the result execution
     */
    public ResultExecution newValue(final String newValue) {
        this.newValue = newValue;
        return this;
    }

}
