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
    private String imgUrl;
    private String link;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
