package org.trams.sicbang.model.enumerate;

/**
 * Created by kiennt on 21/10/16.
 */
public enum UserTypePermission {

    MEMBERSHIP, BROKER, TRUSTED_BROKER;

    public static UserTypePermission getOrNull(String name) {
        for (UserTypePermission type : UserTypePermission.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }

}
