package org.trams.sicbang.model.enumerate;

/**
 * Created by voncount on 4/11/16.
 */
public enum UserType {

    NON_BROKER ,BROKER;

    public static UserType getOrNull(String name) {
        for (UserType type : UserType.values()) {
            if (type.name().equals(name)) {
                return type;
            }
        }
        return null;
    }

}
