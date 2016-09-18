package org.trams.sicbang.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
public class Board extends BaseTimestampEntity {

    private String title;
    @Column(length = 65535, columnDefinition = "Text")
    private String content;

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
}
