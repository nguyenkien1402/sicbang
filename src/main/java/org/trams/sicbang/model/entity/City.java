package org.trams.sicbang.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by voncount on 19/04/2016.
 */
@Entity
@Table(indexes = {
        @Index(name = "CITY_NAME_INDEX", columnList = "name", unique = true)
})
public class City extends BaseEntity {

    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    @OrderBy
    private Collection<District> districts;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }


}
