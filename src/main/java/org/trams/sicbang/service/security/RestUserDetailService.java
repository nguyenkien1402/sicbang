package org.trams.sicbang.service.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.trams.sicbang.model.entity.User;
import org.trams.sicbang.model.entity.UserPermission;
import org.trams.sicbang.model.enumerate.CommonStatus;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;
import org.trams.sicbang.repository.RepositoryUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by voncount on 4/11/16.
 */
@Service
public class RestUserDetailService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(RestUserDetailService.class);

    @Autowired
    private RepositoryUser repositoryUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // check empty, null params
        if (username == null || username.trim().isEmpty()) {
            logger.error("username not valid");
            throw new ApplicationException(MessageResponse.EXCEPTION_BAD_REQUEST);
        }
        // find User
        User user = repositoryUser.findByEmail(username);
        if (user == null) {
            logger.error("username not found");
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_FOUND);
        }
        // check status
        if (!user.getStatus().equals(CommonStatus.ACTIVE)) {
            logger.error("user is inactive");
            throw new ApplicationException(MessageResponse.EXCEPTION_NOT_AVAILABLE);
        }
        // get Permissions
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (UserPermission permission : user.getRole().getPermissions()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission.getName());
            grantedAuthorities.add(authority);
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true, true, grantedAuthorities);
    }

}
