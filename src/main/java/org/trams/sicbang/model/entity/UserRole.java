package org.trams.sicbang.model.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by voncount on 4/8/16.
 */
@Entity
@Table(indexes = {
        @Index(name = "USER_ROLE_NAME_INDEX", columnList = "name", unique = true)
})
public class UserRole extends BaseEntity {

    private String name;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_permission",
            joinColumns = {
                @JoinColumn(name = "role_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "permission_id")
            }
    )
    private Set<UserPermission> permissions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }
}
