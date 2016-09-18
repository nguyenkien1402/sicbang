package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.Mail;
import org.trams.sicbang.model.form.FormMail;

import java.util.List;

/**
 * Created by voncount on 21/04/2016.
 */
public interface IServiceMail {

    /**
     * Send
     * @param mail
     * @return
     */
    void send(FormMail mail);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<Mail> filter(FormMail form);

    /**
     * Delete
     * @param form
     */
    void delete(FormMail form);

    /**
     * Find one
     * @param form
     * @return
     */
    Mail findOne(FormMail form);

    void sendMulti(FormMail form);

}
