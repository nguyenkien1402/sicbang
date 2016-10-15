package org.trams.sicbang.service;

import org.trams.sicbang.model.entity.Category;
import org.trams.sicbang.model.entity.City;
import org.trams.sicbang.model.entity.District;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface IServiceLocation {

    /**
     * Find all city with district and town
     * @return
     */
    List<City> findAllCity();

    List<District> findAllDistrict();
    /**
     * Find all category with business type
     * @return
     */
    List<Category> findAllCategory();

}
