package org.trams.sicbang.model.dto;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by voncount on 4/14/2016.
 */
public class CustomAuthority implements GrantedAuthority {

    private String authority;

    public CustomAuthority() {
    }

    public CustomAuthority(String role) {
        this.authority = role;
    }

    public void setRole(String role) {
        this.authority = role;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
