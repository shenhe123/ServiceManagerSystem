package com.jssg.servicemanagersystem.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final DateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat FORMAT_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");


    /**
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param time
     * @return
     */
    public static String getFullData(long time) {
        return FULL_DATE_FORMAT.format(new Date(time));
    }

    public static String getDateyyMMdd(long time) {
        return FORMAT_yyyy_MM_dd.format(new Date(time));
    }

}

