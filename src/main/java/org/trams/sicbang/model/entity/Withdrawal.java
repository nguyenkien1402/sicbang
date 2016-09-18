package org.trams.sicbang.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by voncount on 4/12/16.
 */
@Entity
public class Withdrawal extends BaseTimestampEntity {

    @Column(length = 65535, columnDefinition = "Text")
    private String reason;

    @OneToOne
    private User user;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
