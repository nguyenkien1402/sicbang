package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Ask;
import org.trams.sicbang.model.form.FormAsk;

/**
 * Created by voncount on 5/4/16.
 */
public interface IServiceAsk {

    /**
     * Create
     * @param form
     * @return
     */
    Ask create(FormAsk form);

    /**
     * Update
     * @param form
     * @return
     */
    Ask update(FormAsk form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Ask> filter(FormAsk form);

    /**
     * Delete
     * @param form
     */
    void delete(FormAsk form);

    /**
     * Find one
     * @param form
     * @return
     */
    Ask findOne(FormAsk form);

}
