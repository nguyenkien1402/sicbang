package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by voncount on 19/04/2016.
 */
@Entity
public class District extends BaseEntity {

    private String name;
    @JsonBackReference
    @ManyToOne
    private City city;
    @JsonManagedReference
    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private Set<Town> towns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Town> getTowns() {
        return towns;
    }

    public void setTowns(Set<Town> towns) {
        this.towns = towns;
    }
}
