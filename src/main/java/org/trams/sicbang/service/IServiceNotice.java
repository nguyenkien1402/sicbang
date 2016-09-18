package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Notice;
import org.trams.sicbang.model.form.FormNotice;

/**
 * Created by voncount on 25/04/2016.
 */
public interface IServiceNotice {

    /**
     * Create
     * @param form
     * @return
     */
    Notice create(FormNotice form);

    /**
     * Update
     * @param form
     * @return
     */
    Notice update(FormNotice form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Notice> filter(FormNotice form);

    /**
     * Delete
     * @param form
     */
    void delete(FormNotice form);

    /**
     * Find one
     * @param form
     * @return
     */
    Notice findOne(FormNotice form);

}
