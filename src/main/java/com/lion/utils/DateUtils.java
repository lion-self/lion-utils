package com.lion.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 */
public class DateUtils {

    /**
     * 将Date类型格式化成指定格式
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /**
     * 将unix时间戳转成指定格式时间
     */
    public static String formatUnixByPattern(long time, String dateFormat) {
        Date date = timeMillisToDate(time);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 将unix时间戳转为date
     */
    public static Date timeMillisToDate(long timeMillis) {
        if(timeMillis < 10000000000L) {
            return new Date(timeMillis * 1000);
        }
        return new Date(timeMillis);
    }

    /**
     * 计算两个时间戳之间相差的天数(2.1=3)
     */
    public static int getTimeDifferenceRoundUp(long sDate, long eDate) {
        return ((int) ((eDate - sDate) / 60 / 60 / 24)) + 1;
    }

    /**
     * 计算两个时间戳之间相差的天数(2.1=2)
     */
    public static int getDaysDifferenceAbandon(long sDate, long eDate) {
        return (int) ((eDate - sDate) / 60 / 60 / 24);
    }

    /**
     * 获取指定日期n个月之后的日期
     */
    public static long getMonthAfter(long date, int n){
        if(n == 0){
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeMillisToDate(date));
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime().getTime();
    }

    /**
     * 获取指定日期n天之后的日期
     */
    public static long getDaysAfter(long date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeMillisToDate(date));
        calendar.add(Calendar.DAY_OF_MONTH, n);
        return calendar.getTime().getTime();
    }

    /**
     * 获取指定日期n小时之后的日期
     */
    public static long getHoursAfter(long date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeMillisToDate(date));
        calendar.add(Calendar.HOUR, n);
        return calendar.getTime().getTime();
    }

    /**
     * 将String类型时间转成时间戳
     */
    public static long dateString2Stamp(String dateString, String pattern) throws ParseException {
        Date date = new SimpleDateFormat(pattern).parse(dateString);
        return date.getTime();
    }

    /**
     * String类型时间转成Date
     */
    public static Date dateString2Date(String date, String format) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.parse(date);
    }

    /**
     * 时间戳转换为指定格式的字符串日期
     */
    public static String timeMillisToStr(long timeMills, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date date;
        if (timeMills > 10000000000L) {
            date = new Date(timeMills);
        } else {
            date = new Date(timeMills * 1000);
        }
        return sf.format(date);
    }

    /**
     * 获取当前时间，并转成指定字符串格式
     */
    public static String getCurrentDate(String format) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(currentTime);
    }

    /**
     * 获取月份的第一天
     * @param format-日期格式：如yyyy/MM/dd
     * @param i-0表示当月第一天，1表示下月第一天....
     * @return
     */
    public static String getMonthFirstDay(String format, int i){
        Calendar cale;
        cale = Calendar.getInstance();
        SimpleDateFormat sFormat = new SimpleDateFormat(format);
        cale.add(Calendar.MONTH, i);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return sFormat.format(cale.getTime());
    }

    /**
     * 获取一天的开始时间
     */
    public static long getDayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 获取月份最后一天
     */
    public static String getMonthLastDay(String format, int i){
        SimpleDateFormat sFormat = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month+i);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date strDateTo = calendar.getTime();
        return sFormat.format(strDateTo);
    }

    /***
     * 根据日期获取月份
     */
    public static String getMonthByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sf1 = new SimpleDateFormat("M");
        return sf1.format(sf.parse(date));
    }

    /***
     * 根据日期获取月份
     */
    public static String getYearByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
        return sf1.format(sf.parse(date));
    }

    /***
     * 根据日期获取月周期
     */
    public static String getMonthCycleByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sf1 = new SimpleDateFormat("MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sf.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDayOfMonth = calendar.getTime();
        return sf.format(firstDayOfMonth) + "-" + sf.format(lastDayOfMonth);
    }

    /***
     * 根据日期获取月第一天
     */
    public static Long getMonthFirstDayByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sf.parse(date));
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = calendar.getTime();
        return firstDayOfMonth.getTime();
    }

    /***
     * 根据日期获取下月第一天
     */
    public static Long getMonthLastDayByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sf.parse(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 2);
        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth.getTime();
    }

    /***
     * 根据日期获取年月
     */
    public static String getYearAndMonthByDate(String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy/MM/");
        return sf1.format( sf.parse(date));
    }

    /***
     * 判断一个时间是不是昨天
     */
    public static boolean isYesterday(Date date) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - dateCalendar.get(Calendar.DAY_OF_YEAR) == 1;
    }

}
