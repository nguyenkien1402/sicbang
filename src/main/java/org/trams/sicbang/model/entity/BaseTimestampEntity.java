package org.trams.sicbang.model.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by voncount on 4/8/16.
 */
@MappedSuperclass
public abstract class BaseTimestampEntity extends BaseEntity {

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @PrePersist
    protected void prePersist() {
        Date currentDate = new Date();
        this.createdDate = currentDate;
        this.modifiedDate = currentDate;
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedDate = new Date();
    }
}
