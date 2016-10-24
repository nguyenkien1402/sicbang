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
    private Ask ask;

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

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }
}
