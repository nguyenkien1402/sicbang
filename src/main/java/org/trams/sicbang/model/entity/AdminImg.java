package org.trams.sicbang.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Base64;
import java.util.Set;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
@ToString
public class AdminImg extends BaseTimestampEntity {

    @Getter @Setter
    private String url;
    @Transient
    private Set<MultipartFile> attachments;

    public Set<MultipartFile> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<MultipartFile> attachments) {
        this.attachments = attachments;
    }

}
