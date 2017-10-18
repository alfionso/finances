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
package es.alfonsomarin.finances.core.domain.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.alfonsomarin.finances.core.ApplicationConfig;
import es.alfonsomarin.finances.core.domain.common.exception.MessageCodes;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Transaction
 *
 * @author alfonso.marin.lopez
 */
public class Transaction {
    
    private Long id;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = ApplicationConfig.DATETIME_FORMAT)
    @DateTimeFormat(pattern=ApplicationConfig.DATETIME_FORMAT)
    private Date dateCreation;
    
    @NotNull(message = MessageCodes.VALIDATION_PATH_MANDATORY)
    private String description;
    
    private String  category;

    private String type;
    
    @JsonFormat(shape=JsonFormat.Shape.NUMBER_FLOAT)
    private BigDecimal value;


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
     * Gets date Creation.
     *
     * @return the date Creation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * Sets date Creation.
     *
     * @param dateCreation the date Creation
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Id transaction.
     *
     * @param id the id
     * @return the transaction
     */
    public Transaction id(final Long id) {
        this.id = id;
        return this;
    }

    /**
     * Date creation transaction.
     *
     * @param dateCreation the dateCreation
     * @return the transaction
     */
    public Transaction dateCreation(final Date dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    /**
     * Description transaction.
     *
     * @param description the description
     * @return the transaction
     */
    public Transaction description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Category transaction.
     *
     * @param category the category
     * @return the transaction
     */
    public Transaction category(final String category) {
        this.category = category;
        return this;
    }

    /**
     * Type transaction.
     *
     * @param type the type
     * @return the transaction
     */
    public Transaction type(final String type) {
        this.type = type;
        return this;
    }

    /**
     * Value transaction.
     *
     * @param value the value
     * @return the transaction
     */
    public Transaction value(final BigDecimal value) {
        this.value = value;
        return this;
    }
}
