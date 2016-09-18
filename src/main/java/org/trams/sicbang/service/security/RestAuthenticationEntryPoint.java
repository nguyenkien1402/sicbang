package org.trams.sicbang.service.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;
import org.trams.sicbang.common.utils.ServletUtils;
import org.trams.sicbang.model.enumerate.MessageResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by voncount on 4/8/16.
 */
@Service
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String path = request.getRequestURI();
        if (path.startsWith("/api")) {
            ServletUtils.response(response, HttpServletResponse.SC_FORBIDDEN, MessageResponse.EXCEPTION_UNAUTHORIZED);
        }
        else if (path.startsWith("/admin")) {
            response.sendRedirect("/admin");
        }
        else if (path.startsWith("/member")) {
            response.sendRedirect("/");
        }
        else {
            response.sendRedirect("/error");
        }
    }

}
