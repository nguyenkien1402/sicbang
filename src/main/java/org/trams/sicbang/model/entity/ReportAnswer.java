package org.trams.sicbang.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by voncount on 25/04/2016.
 */
@Entity
public class ReportAnswer extends BaseTimestampEntity {

    private String title;
    @Column(length = 65535, columnDefinition = "Text")
    private String content;

    @OneToOne
    private ReportInformation reportInformation;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReportInformation getReportInformation() {
        return reportInformation;
    }

    public void setReportInformation(ReportInformation reportInformation) {
        this.reportInformation = reportInformation;
    }
}
