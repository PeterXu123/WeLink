package edu.neu.madcourse.welink.utility;

import java.text.SimpleDateFormat;

public class Formatter {
    public static SimpleDateFormat STORAGE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

    public static String formateLocationString(Double latitude, Double longitude) {
        String s1 = Long.toString((long)(double)latitude);
        String s2 = Double.toString(latitude - (long)(double)latitude).substring(2,4);
        String s3 = Long.toString((long)(double)longitude);
        String s4 = Double.toString(Math.abs(longitude - (long)(double)longitude)).substring(2,4);
        return s1+"_"+s2+'_'+s3+"_"+s4;
    }
}
