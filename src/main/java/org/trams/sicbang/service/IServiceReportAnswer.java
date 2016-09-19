package org.trams.sicbang.service;

import org.springframework.data.domain.Page;
import org.trams.sicbang.model.entity.ReportAnswer;
import org.trams.sicbang.model.form.FormReportAnswer;

/**
 * Created by voncount on 22/04/2016.
 */
public interface IServiceReportAnswer {

    /**
     * Create
     * @param form
     * @return
     */
    ReportAnswer create(FormReportAnswer form);

    /**
     * Update
     * @param form
     * @return
     */
    ReportAnswer update(FormReportAnswer form);

    /**
     * Filter
     * @param form
     * @return
     */
    Page<ReportAnswer> filter(FormReportAnswer form);

    /**
     * Delete
     * @param form
     */
    void delete(FormReportAnswer form);

    /**
     * Find one
     * @param form
     * @return
     */
    ReportAnswer findOne(FormReportAnswer form);
}
