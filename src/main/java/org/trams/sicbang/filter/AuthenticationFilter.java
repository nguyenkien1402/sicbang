package org.trams.sicbang.filter;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.trams.sicbang.common.utils.JsonUtils;
import org.trams.sicbang.common.utils.ServletUtils;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.enumerate.MessageResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by voncount on 5/16/16.
 */
public class AuthenticationFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Init Authentication filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            CustomUserDetail userDetail = ServletUtils.extractCredential(request);
            if (!userDetail.isCredentialsNonExpired()) {
                throw new BadCredentialsException("Token is expired");
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(JsonUtils.toString(new Response(MessageResponse.EXCEPTION_UNAUTHORIZED)));
        }
    }

    @Override
    public void destroy() {
        logger.info("Destroy Authentication filter");
    }

}
