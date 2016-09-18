package org.trams.sicbang.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by voncount on 4/13/2016.
 */
public class DateTimeUtils {

    static final DateFormat fullDateTime;

    static {
        fullDateTime = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    }

    public static long add(int type, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(type, value);
        return calendar.getTime().getTime();
    }

    public static boolean isNoneExpired(long time) {
        Calendar current = Calendar.getInstance();
        Calendar compare = Calendar.getInstance();
        compare.setTimeInMillis(time);
        return current.compareTo(compare) < 0;
    }

    public static String formatFullDateTime(Date date) {
        return fullDateTime.format(date);
    }

}
