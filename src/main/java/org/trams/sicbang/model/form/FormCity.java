package org.trams.sicbang.model.form;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.jpa.domain.Specification;
import org.trams.sicbang.model.dto.BaseFormSearch;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.City;

/**
 * Created by voncount on 5/4/16.
 */
public class FormCity extends BaseFormSearch<City> {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Specification<City> getSpecification() {
        return null;
    }
}
