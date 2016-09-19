package org.trams.sicbang.model.entity;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
public class Mail extends BaseTimestampEntity {

    private String mailSubject;
    private String mailTo;
    @Column(length = 65535, columnDefinition = "Text")
    private String mailContent;
//    @Transient
//    private Set<MultipartFile> attachments;

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

//    public Set<MultipartFile> getAttachments() {
//        return attachments;
//    }
//
//    public void setAttachments(Set<MultipartFile> attachments) {
//        this.attachments = attachments;
//    }

//    @PrePersist
    public void encodeContent() {
        try {
            this.mailContent = Base64.getEncoder().encodeToString(mailContent.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @PostLoad
    public void decodeContent() {
        String beforeDeCode =  mailContent;
        byte[] decodedStr = Base64.getDecoder().decode( beforeDeCode );
        String decode = null;
        try {
            decode = new String(decodedStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mailContent = decode;
    }
}
