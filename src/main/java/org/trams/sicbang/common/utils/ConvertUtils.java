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

    public static double getDistanceFromLatLonInKm(double lat1,double lng1,double lat2,double lng2) {
        long R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    public static double getDistanceByZoomLevel(int zoomLevel){
        switch (zoomLevel){
            case 1: return 0.3;
            case 2: return 0.45;
            case 3: return 0.75;
            case 4: return 1.5;
            case 5: return 3;
            case 6: return 6;
            case 7: return 10;
            case 8: return 20;
            case 9: return 50;
            case 10: return 80;
            case 11: return 140;
            case 12: return 288;
            case 13: return 400;
            case 14: return 500;
            default:return 0;
        }
    }

    public static double getDistanceByZoomLevelAPI(int zoomLevel){
        switch (zoomLevel){
            case -1: return 0.06;
            case -0: return 0.09;
            case 1:  return 0.17;
            case 2: return 0.3;
            case 3: return 0.75;
            case 4:  return 1.5;
            case 5: return 3;
            case 6: return 6;
            case 7:  return 12;
            case 8: return 24;
            case 9: return 48;
            case 10:  return 96;
            case 11: return 192;
            default: return 0;
        }
    }


}
