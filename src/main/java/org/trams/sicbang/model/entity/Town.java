package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by voncount on 19/04/2016.
 */
@Entity
public class Town extends BaseEntity {

    private String name;
    @JsonBackReference
    @ManyToOne
    private District district;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
