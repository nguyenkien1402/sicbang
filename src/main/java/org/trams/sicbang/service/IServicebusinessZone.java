package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Sanggun;
import org.trams.sicbang.model.form.FormBusiness;

import java.util.List;

/**
 * Created by voncount on 19/04/2016.
 */
public interface IServicebusinessZone {

    /**
     * Filter
     * @param form
     * @return
     */
    List<Sanggun> filter(FormBusiness form);

    List<Sanggun> findAll();

    /**
     *
     * @param form
     * @return
     */
    Page<Sanggun> basicFilter(FormBusiness form);

    List<String>findTownList(String city);

    List<String>findDistinctSanggunBycityDegree(String city);

    List<String> findDongList(String city, String town);
}
