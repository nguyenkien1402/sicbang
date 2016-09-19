package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by voncount on 4/12/16.
 */
@Entity
@Table(indexes = {
        @Index(name = "BUSINESS_TYPE_NAME_INDEX", columnList = "name", unique = true)
})
public class BusinessType extends BaseEntity {

    private String name;
    @JsonBackReference
    @ManyToOne
    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
