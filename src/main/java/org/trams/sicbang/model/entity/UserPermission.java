package org.trams.sicbang.model.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
@Table(indexes = {
        @Index(name = "USER_PERMISSION_NAME_INDEX", columnList = "name", unique = true)
})
public class UserPermission extends BaseEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
