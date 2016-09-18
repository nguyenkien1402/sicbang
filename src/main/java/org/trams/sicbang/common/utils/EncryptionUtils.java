package org.trams.sicbang.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Created by voncount on 4/13/2016.
 */
public class EncryptionUtils {

    public static final String JWT_KEY = "MinhDangLe";

    /**
     * Encrypt with jwt
     * @param subject
     * @return
     */
    public static String jwtBuild(Object subject) {
        return Jwts.builder()
                .setSubject(JsonUtils.toString(subject))
                .signWith(SignatureAlgorithm.HS512, JWT_KEY.getBytes())
                .compact();
    }

    /**
     * Decrypt with jwt
     * and convert to klass T
     * @param jwtToken
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> T jwtParse(String jwtToken, Class<T> klass) {
        String parse = Jwts.parser()
                .setSigningKey(JWT_KEY.getBytes())
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
        return JsonUtils.from(parse, klass);
    }

}