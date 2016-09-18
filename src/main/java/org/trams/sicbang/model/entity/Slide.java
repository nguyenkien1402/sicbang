package org.trams.sicbang.model.entity;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Kien on 8/18/2016.
 */

@Entity
@Table(indexes = {@Index(name="ID_SLIDE_INDEX", columnList = "id", unique = true)})
public class Slide extends BaseTimestampEntity{

    private String type;
    private String webUrl;
    private String appUrl;
    private String name;

    @Transient
    private MultipartFile attachments;

    @ManyToOne
    private User user;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MultipartFile getAttachments() {
        return attachments;
    }

    public void setAttachments(MultipartFile attachments) {
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
