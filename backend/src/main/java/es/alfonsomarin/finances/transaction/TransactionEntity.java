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

package es.alfonsomarin.finances.transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Transaction Entity
 *
 * @author alfonso.marin.lopez
 */
@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private Date dateCreation;

    @Column
    private String description;

    @Column
    private String category;

    @Column
    private String type;

    @Column
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
     * Gets dateCreation.
     *
     * @return the dateCreation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * Sets dateCreation.
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
     * Id transaction entity.
     *
     * @param id the id
     * @return the transaction entity
     */
    public TransactionEntity id(final Long id) {
        this.id = id;
        return this;
    }


    /**
     * Date transaction entity.
     *
     * @param dateCreation the date Creation
     * @return the transaction entity
     */
    public TransactionEntity dateCreation(final Date dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    /**
     * Description transaction entity.
     *
     * @param description the description
     * @return the transaction entity
     */
    public TransactionEntity description(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Category transaction entity.
     *
     * @param category the category
     * @return the transaction entity
     */
    public TransactionEntity category(final String category) {
        this.category = category;
        return this;
    }

    /**
     * Type transaction entity.
     *
     * @param type the type
     * @return the transaction entity
     */
    public TransactionEntity type(final String type) {
        this.type = type;
        return this;
    }

    /**
     * Value transaction entity.
     *
     * @param value the value
     * @return the transaction entity
     */
    public TransactionEntity value(final BigDecimal value) {
        this.value = value;
        return this;
    }
}
