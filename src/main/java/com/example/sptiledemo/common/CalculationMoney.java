package com.example.sptiledemo.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculationMoney {

    public static Date randomDate(String beginDate,String endDate ){

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date start = format.parse(beginDate);//构造开始日期

            Date end = format.parse(endDate);//构造结束日期

//getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。

            if(start.getTime() >= end.getTime()){

                return null;

            }

            long date = random(start.getTime(),end.getTime());

            return new Date(date);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

    private static long random(long begin,long end){

        long rtn = begin + (long)(Math.random() * (end - begin));

//如果返回的是开始时间和结束时间，则递归调用本函数查找随机值

        if(rtn == begin || rtn == end){

            return random(begin,end);

        }

        return rtn;

    }




    public static String getOnTex(String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal multiply = bigDecimal.divide(new BigDecimal("1.06"), 6);
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(multiply);
        return format;
    }


    public static String getEndTime(String name, String getqTime, int i, Double aDouble) {
        String[] split = name.split("-");
        String[] sp = getqTime.split("-");
        String endTime = null;
        Integer in = Integer.valueOf(sp[2]);
        Integer mon = Integer.valueOf(sp[1]);
        Integer year = Integer.valueOf(sp[0]);
        Integer pt = 0;
        if (aDouble.equals(Double.valueOf("12.0"))) {
            pt = 12;
        } else if (aDouble.equals(Double.valueOf("24.0"))) {
            pt = 24;
        }
        if (split[0].equals("YXZ")) {
            i = i - pt;
        }
        if (in > 15) {
            in = 1;
            mon = mon + 1;
        }
        if (mon + i > 12) {
            mon = mon + i - 12;
            if (mon > 12) {
                mon = mon - 12;
                year = year + 1;
            }
            year = year + 1;
            if (mon + i > 12) {
                mon = mon + i - 12;
                if (mon > 12) {
                    mon = mon - 12;
                    year = year + 1;
                }
                year = year + 1;
                if (mon + i > 12) {
                    mon = mon + i - 12;
                    if (mon > 12) {
                        mon = mon - 12;
                        year = year + 1;
                    }
                    year = year + 1;
                }
            }
        } else {
            mon = mon + i;
        }
        endTime = year.toString() + "-" + mon.toString() + "-" + "01";
        return endTime;
    }

}
