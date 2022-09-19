package com.basic.happytest.modules.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关工具类
 * @author lhf
 */

public class TimeUtils {

    /**
     * 纪元 年-月-日  时-分-秒-毫秒 周几 一年中第几天 一年中第几周 一个月中第几周 A.M/P.M标记 一天中的小时 A.M/P.M下的小时 时区
     * 公元 2021-12-01 10:57:11:590 星期三 335 49 1 上午 10 10 CST
     */
    public static final String DATE_DETAIL_PATTERN = "G yyyy-MM-dd hh:mm:ss:SSS E D w W a k K z";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 以指定的格式输出日期
     * @param timestamp 日期对应的时间戳
     * @param pattern 格式
     */
    public static void printTime(Long timestamp, String pattern) {
        Date date = new Date(timestamp);
        printTime(date, pattern);
    }

    /**
     * 以指定的格式输出日期
     * @param date 日期
     * @param pattern 格式
     */
    public static void printTime(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        System.out.println(simpleDateFormat.format(date));
        // System.out.printf("全部日期和时间信息：%tc \n", date);
        // System.out.printf("年-月-日格式：%tF \n", date);
        // System.out.printf("月/日/年格式：%tD \n", date);
        // System.out.printf("HH:MM:SS PM格式（12时制）：%tr \n", date);
        // System.out.printf("HH:MM:SS格式（24时制）：%tT \n", date);
        // System.out.printf("HH:MM格式（24时制）：%tR \n", date);
    }

    /**
     * 获取日期对应的季节
     * @param date 日期
     * @return 季节（1-4）
     */
    public static Integer getSeason(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 将标准格式的日期字符串转换为日期对象
     * @param dateInStr 字符串格式的日期
     * @param pattern 日期的格式标准
     * @return 日期对象
     * @throws ParseException 解析异常
     */
    public static Date parseTime(String dateInStr, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(dateInStr);
        System.out.println(simpleDateFormat.format(date));
        return date;
    }

    /**
     * 获取日期对应的当年的1号的零点时间
     * @param date 日期
     * @return 日期对应的当年的1号的零点时间
     */
    public static Date getTimeOfJanuary1stZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        Date timeOfJanuary1stZeroTime = calendar.getTime();
        printTime(timeOfJanuary1stZeroTime, DATE_TIME_PATTERN);
        return timeOfJanuary1stZeroTime;
    }

    /**
     * 获取日期对应的当年的最后一刻时间
     * @param date 日期
     * @return 日期对应的当年的最后一刻时间
     */
    public static Date getLastTimeOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        Date lastTimeOfYear = calendar.getTime();
        printTime(lastTimeOfYear, DATE_TIME_PATTERN);
        return lastTimeOfYear;
    }

    /**
     * 获取日期对应的前一天的零点时间
     * @param date 日期
     * @return 日期对应的前一天的零点时间
     */
    public static Date getYesterdayZeroTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        Date yesterdayZeroTime = calendar.getTime();
        printTime(yesterdayZeroTime, DATE_TIME_PATTERN);
        return yesterdayZeroTime;
    }

    /**
     * 获取日期对应的前一天的最后一刻时间
     * @param date 日期
     * @return 日期对应的前一天的最后一刻时间
     */
    public static Date getYesterdayLastTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND) - 1);
        Date yesterdayLastTime = calendar.getTime();
        printTime(yesterdayLastTime, DATE_TIME_PATTERN);
        return yesterdayLastTime;
    }

    /**
     * 获取日期对应上一个月的起始时间
     * @param date 日期
     * @return 日期对应的上一个月的起始时间
     */
    public static Date getLastMonth1stZeroTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        Date lastMonth1stZeroTime = calendar.getTime();
        printTime(lastMonth1stZeroTime, DATE_TIME_PATTERN);
        return lastMonth1stZeroTime;
    }

    /**
     * 获取日期对应的上一个月的最后一刻时间
     * @param date 日期
     * @return 日期对应的上一个月的最后一刻时间
     */
    public static Date getLastMonthLastTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND) - 1);
        Date lastMonthLastTime = calendar.getTime();
        printTime(lastMonthLastTime, DATE_TIME_PATTERN);
        return lastMonthLastTime;
    }

    /**
     * 获取按天计算的时间间隔（忽略时间先后顺序）
     * @param date1 日期1
     * @param date2 日期2
     * @return 两个日期的按天算的时间间隔
     */
    public static Long getDayInterval(Date date1, Date date2) {
        Date begin, end;
        if(date1.before(date2)){
            begin = date1;
            end = date2;
        } else {
            begin = date2;
            end = date1;
        }
        LocalDateTime beginTime = begin.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        // 通过使用不同的ChronoUnit，可以获得以不同单位计算的时间间隔
        // beginTime.until(endTime, ChronoUnit.YEARS);
        // beginTime.until(endTime, ChronoUnit.MONTHS);
        return beginTime.until(endTime, ChronoUnit.DAYS);
    }

    /**
     * 按年增加日期
     * @param date 日期
     * @param years 增加的年份（可为负数，为负数时表示减少年份）
     * @return 增加年份后的日期
     */
    public static Date addYears(Date date, Integer years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /**
     * 按月增加日期
     * @param date 日期
     * @param months 增加的月份（可为负数，为负数时表示减少月数）
     * @return 增加月数后的日期
     */
    public static Date addMonths(Date date, Integer months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 按天增加日期
     * @param date 日期
     * @param days 增加的天数（可为负数，为负数时表示减少天数）
     * @return 增加天数后的日期
     */
    public static Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
}
