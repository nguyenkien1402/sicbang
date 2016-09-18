package org.trams.sicbang.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.trams.sicbang.model.enumerate.MessageResponse;
import org.trams.sicbang.model.exception.ApplicationException;

/**
 * Created by voncount on 4/6/16.
 */
public class JsonUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new ApplicationException(e, MessageResponse.EXCEPTION_PARSE_JSON);
        }
    }

    public static <T> T from(String json, Class<T> klass) {
        try {
            return mapper.readValue(json, klass);
        } catch (Exception e) {
            throw new ApplicationException(e, MessageResponse.EXCEPTION_PARSE_JSON);
        }
    }

}
