package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by voncount on 4/12/16.
 */
@Entity
@Table(indexes = {
        @Index(name = "CATEGORY_NAME_INDEX", columnList = "name", unique = true)
})
public class Category extends BaseEntity {

    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<BusinessType> businessTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(Set<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }
}
