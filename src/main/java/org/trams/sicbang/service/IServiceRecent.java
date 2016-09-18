package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Recent;
import org.trams.sicbang.model.form.FormRecent;

/**
 * Created by voncount on 22/04/2016.
 */
public interface IServiceRecent {

    /**
     * Create
     * @param form
     * @return
     */
    Recent create(FormRecent form);

    /**
     * Update
     * @param form
     * @return
     */
    Recent update(FormRecent form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Recent> filter(FormRecent form);

    /**
     * Delete
     * @param form
     */
    void delete(FormRecent form);

    /**
     * findOne
     * @param formRecent
     */
    Recent findOne(FormRecent formRecent);
}
