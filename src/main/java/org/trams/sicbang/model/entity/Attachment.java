package org.trams.sicbang.model.entity;

import javax.persistence.Entity;

/**
 * Created by voncount on 4/12/16.
 */
@Entity
public class Attachment extends BaseEntity {

    private String tableRef;
    private Long rowRef;
    private String name;
    private String thumbnail;
    private String origin;

    public String getTableRef() {
        return tableRef;
    }

    public void setTableRef(String tableRef) {
        this.tableRef = tableRef;
    }

    public Long getRowRef() {
        return rowRef;
    }

    public void setRowRef(Long rowRef) {
        this.rowRef = rowRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
