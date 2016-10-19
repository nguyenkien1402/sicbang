package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.form.FormAsk;
import org.trams.sicbang.model.form.FormCity;

import java.util.Collection;
import java.util.List;

/**
 * Created by voncount on 5/4/16.
 */
public interface IServiceCity {

    /**
     * Filter
     * @param form
     * @return
     */
    Page<City> filter(FormCity form);
    /**
     * Find one
     * @param form
     * @return
     */

    City findOne(FormCity form);

    List<City> findAll();

}
