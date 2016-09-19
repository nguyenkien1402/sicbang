package org.trams.sicbang.common.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.trams.sicbang.model.dto.CustomUserDetail;
import org.trams.sicbang.model.dto.Response;
import org.trams.sicbang.model.enumerate.MessageResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by voncount on 4/11/16.
 */
public class ServletUtils {

    public static void response(HttpServletResponse response, int code, MessageResponse message) throws IOException {
        response.setStatus(code);
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JsonUtils.toString(new Response(message)));
    }

    public static CustomUserDetail extractCredential(HttpServletRequest servletRequest) throws Exception {
        String token = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return EncryptionUtils.jwtParse(token, CustomUserDetail.class);
    }

}
