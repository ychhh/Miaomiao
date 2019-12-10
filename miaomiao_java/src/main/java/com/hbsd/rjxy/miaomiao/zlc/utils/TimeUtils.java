package com.hbsd.rjxy.miaomiao.zlc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {


    /*
        HH:MM:ss        ss必须小写，不然会出现三位秒

     */
    public static String getTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        return sdf.format(date);
    }
}
