package org.trams.sicbang.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by voncount on 5/4/16.
 */
@Entity
public class Ask extends BaseTimestampEntity {

    private String title;
    @Column(length = 65535, columnDefinition = "Text")
    private String content;
    private String name;
    private String contact;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
