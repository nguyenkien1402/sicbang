package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.form.FormAdminImg;
import org.trams.sicbang.model.form.FormAdminImg;

/**
 * Created by voncount on 21/04/2016.
 */
public interface IServiceAdmin {

    /**
     * Send
     * @param form
     * @return
     */
    void create(FormAdminImg form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Mail> filter(FormAdminImg form);

    /**
     * Delete
     * @param form
     */
    void delete(FormAdminImg form);

    /**
     * Find one
     * @param form
     * @return
     */
    Mail findOne(FormAdminImg form);

}
