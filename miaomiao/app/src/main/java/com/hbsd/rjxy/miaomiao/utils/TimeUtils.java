package com.hbsd.rjxy.miaomiao.utils;

import java.util.Calendar;


/**
 * TODO     时间工具类
 */
public class TimeUtils {



    public TimeUtils(String currentTime){
        String[] day_time = currentTime.split(" ");
        String[] ymd = day_time[0].split("-");
        this.year = Integer.parseInt(ymd[0]);
        this.month = Integer.parseInt(ymd[1]);
        this.day = Integer.parseInt(ymd[2]);

        String[] hms = day_time[1].split(":");
        this.hour = Integer.parseInt(hms[0]);
        this.minute = Integer.parseInt(hms[1]);
        this.second = Integer.parseInt(hms[2]);
    }

    /**
     *  服务器的时间
     */
    private int year;
    private int month;
    private int day;

    private int hour;
    private int minute;
    private int second;


    @Override
    public String toString() {
        return "TimeUtils{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }


    public String compareTime(String dataTime){
        String[] day_time = dataTime.split(" ");
        String[] ymd = day_time[0].split("-");
        String[] hms = day_time[1].split(":");
        //计算天数差距
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,hour,minute,second);
        long currentMillis = calendar.getTimeInMillis();
        calendar.set(Integer.parseInt(ymd[0])
                ,Integer.parseInt(ymd[1])
                ,Integer.parseInt(ymd[2])
                ,Integer.parseInt(hms[0])
                ,Integer.parseInt(hms[1])
                ,Integer.parseInt(hms[2]));
        long dataMillis = calendar.getTimeInMillis();
        long millisBetween = currentMillis-dataMillis;
        long between = (currentMillis-dataMillis)/(1000*3600*24);
        int betweenDays = Integer.parseInt(String.valueOf(between));

        if(betweenDays>=365){
            //跨年了
            return (betweenDays/365)+"年前";
        }else{
            //在同一年以内，我要判断是否在一个月以内
            if(betweenDays>=30){
                return (betweenDays/30)+"个月以前";
            }else{
                //一个月内，我要判断是否是同一天
                if(millisBetween<86400000){
                    //同一天之内,判断是否在同一个小时
                    if(millisBetween<3600000){
                        //一个小时以内
                        if(millisBetween<60*1000){
                            //一分钟之内
                            return "刚刚";
                        }else{
                            return millisBetween/(60*1000)+"分钟前";
                        }
                    }else{
                        //不在一个小时以内
                        return millisBetween/(1000*3600)+"小时前";
                    }
                }else{
                    //不是同一天，我要去计算是多少天以前
                    if(between<5){
                        return betweenDays+"天前";
                    }else{
                        //超过四天
                        return ymd[1]+"-"+ymd[2];
                    }
                }

            }
        }
    }
}
