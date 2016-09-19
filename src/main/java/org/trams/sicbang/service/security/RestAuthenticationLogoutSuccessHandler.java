package org.trams.sicbang.service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by voncount on 4/12/16.
 */
@Service
public class RestAuthenticationLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String path = request.getHeader("Referer");
        if (path.contains("/admin")) {
            response.sendRedirect("/admin");
        }
        else {
            response.sendRedirect("/");
        }
    }

}