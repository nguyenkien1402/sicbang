package org.trams.sicbang.service.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by voncount on 4/8/16.
 */
@Service
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String path = request.getHeader("Referer");
        if (path.contains("/admin")) {
            response.sendRedirect("/admin?login_error=" + "Invalid user name or password");
        }
        else {
            if(exception.getMessage().equals("Resource Broker not available")) {
                response.getWriter().write("broker_not_available");
                return;
            }
            response.getWriter().write("login_error");
        }
    }
}
