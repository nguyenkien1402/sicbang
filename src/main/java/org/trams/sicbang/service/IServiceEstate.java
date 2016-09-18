package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.entity.Estate;
import org.trams.sicbang.model.form.FormEstate;

/**
 * Created by voncount on 4/13/2016.
 */
public interface IServiceEstate {

    /**
     * Create
     *
     * @param form
     * @return
     */
    Estate create(FormEstate form);

    /**
     * Update
     *
     * @param form
     * @return
     */
    Estate update(FormEstate form);

    /**
     * Find one
     *
     * @param form
     * @return
     */
    Estate findOne(FormEstate form);

    /**
     * Filter
     *
     * @param form
     * @return
     */
    Page<Estate> filter(FormEstate form);

    /**
     * @param form
     * @param user
     * @return
     */
    Page<Estate> filter(FormEstate form, CustomUserDetail user);

    Page<Estate> filterAddr(FormEstate formEstate);

    /**
     * Delete
     * @param form
     */
    void delete(FormEstate form);

    /*
    * Update
    * @Param form
    */
    Estate updateEstateType(FormEstate form, String type);

    /*
    * Update Estate
     */
    Estate updateEstate(FormEstate form, Estate estate);
}
