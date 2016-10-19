package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.entity.Town;
import org.trams.sicbang.model.form.FormAsk;
import org.trams.sicbang.model.form.FormTown;

import java.util.List;

/**
 * Created by kientnt on 10/17/16.
 */
public interface IServiceTown {


    /**
     * Filter
     * @param form
     * @return
     */
    Page<Town> filter(FormTown form);

    /**
     * Find one
     * @param form
     * @return
     */
    Town findOne(FormTown form);

    List<Town> findAll();

}
