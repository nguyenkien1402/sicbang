package org.trams.sicbang.interceptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by voncount on 5/16/16.
 */
@Component
public class DebugInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = Logger.getLogger(DebugInterceptor.class);

    @Autowired
    Environment env;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (determineActiveProfile()) {
                StringBuilder sb = new StringBuilder("\n");
                sb.append("===================================================================================================").append("\n")
                        .append("Request Info:").append("\n")
                        .append("URL            : " + request.getRequestURL().toString()).append("\n")
                        .append("URI            : " + request.getRequestURI()).append("\n")
                        .append("Referer        : " + request.getHeader("referer")).append("\n")
                        .append("Method         : " + request.getMethod()).append("\n")
                        .append("IP             : " + request.getRemoteAddr()).append("\n")
                        .append("User Agent     : " + request.getHeader("User-Agent")).append("\n")
                        .append("Params         : ").append("\n");

                Enumeration<String> parameterNames = request.getParameterNames();
                String key;
                while (parameterNames.hasMoreElements()) {
                    key = parameterNames.nextElement();
                    sb
                            .append(key).append("\t").append("=").append("\t")
                            .append(request.getParameter(key)).append("\n");
                }

                sb.append("===================================================================================================").append("\n");
                logger.info(sb.toString());
            }
        } catch (Exception e) {
            logger.error("Logging request failed, caused by: " + e.getMessage());
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            if (determineActiveProfile()) {
                StringBuilder sb = new StringBuilder("\n");
                sb.append("===================================================================================================").append("\n")
                        .append("Response Info:").append("\n")
                        .append("URL            : " + request.getRequestURL().toString()).append("\n")
                        .append("URI            : " + request.getRequestURI()).append("\n")
                        .append("Referer        : " + request.getHeader("referer")).append("\n")
                        .append("Method         : " + request.getMethod()).append("\n")
                        .append("IP             : " + request.getRemoteAddr()).append("\n")
                        .append("User Agent     : " + request.getHeader("User-Agent")).append("\n")
                        .append("Model          : ").append("\n");

                Iterator<String> iterator = modelAndView.getModel().keySet().iterator();
                String key;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    sb
                            .append(key).append("\t").append("=").append("\t")
                            .append(modelAndView.getModel().get(key)).append("\n");
                }
                sb.append("View         : " + modelAndView.getViewName()).append("\n");
                sb.append("===================================================================================================").append("\n");
                logger.info(sb.toString());
            }
        } catch (Exception e) {
            logger.error("Logging response failed, caused by: " + e.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    public boolean determineActiveProfile() {
        return Arrays.asList(env.getActiveProfiles()).contains(env.getProperty("profile.interceptor", "prod"));
    }

}
