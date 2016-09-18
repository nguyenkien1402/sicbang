package org.trams.sicbang.common.utils;

import java.util.Date;
import java.util.Optional;

/**
 * Created by voncount on 5/4/16.
 */
public class ConvertUtils {

    public static Optional<Integer> toIntNumber(String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> toLongNumber(String value) {
        try {
            return Optional.of(Long.parseLong(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> toDoubleNumber(String value) {
        try {
            return Optional.of(Double.parseDouble(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> toBoolean(String value) {
        try {
            if (value == null || "".equals(value)) return Optional.empty();
            return Optional.of(Boolean.parseBoolean(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Date> toDate(String timestamp) {
        try {
            return Optional.of(new Date(Long.parseLong(timestamp)));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T extends Enum<T>> Optional<T> toEnum(String value, Class<T> klass) {
        try {
            return Optional.of(Enum.valueOf(klass, value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
