package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.ReportInformation;
import org.trams.sicbang.model.form.FormReport;

/**
 * Created by voncount on 22/04/2016.
 */
public interface IServiceReport {

    /**
     * Create
     * @param form
     * @return
     */
    ReportInformation create(FormReport form);

    /**
     * Update
     * @param form
     * @return
     */
    ReportInformation update(FormReport form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<ReportInformation> filter(FormReport form);

    /**
     * Delete
     * @param form
     */
    void delete(FormReport form);

    /**
     * Find one
     * @param form
     * @return
     */
    ReportInformation findOne(FormReport form);
}
