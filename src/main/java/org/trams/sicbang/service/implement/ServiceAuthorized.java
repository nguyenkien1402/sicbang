package org.trams.sicbang.service.implement;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by voncount on 4/11/16.
 */
@Service
public class ServiceAuthorized {

    public Authentication isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken ? null : auth;
    }

    public boolean isAuthorized(String username) {
        if (isAuthenticated() == null) {
            return false;
        } else {
            return isAuthenticated().getName().equals(username);
        }
    }

    public boolean isAdmin() {
        if (isAuthenticated() == null) {
            return false;
        } else {
            Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) isAuthenticated().getAuthorities();
            if (authorities == null || authorities.isEmpty()) return false;
            else return authorities.contains(new SimpleGrantedAuthority("ADMIN"));
        }
    }

}
