package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.District;
import org.trams.sicbang.model.form.FormAsk;
import org.trams.sicbang.model.form.FormDistrict;

import java.util.List;

/**
 * Created by voncount on 5/4/16.
 */
public interface IServiceDistrict {

    /**
     * Filter
     * @param form
     * @return
     */
    Page<District> filter(FormDistrict form);


    /**
     * Find one
     * @param form
     * @return
     */
    District findOne(FormDistrict form);

    List<District> findAll();

    List<District> findByCityId(int cityId);
}