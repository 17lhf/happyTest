package com.basic.happytest.modules.time;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

class TimeUtilsTest {

    @Test
    public void testPrintTime() {
        Date date = new Date();
        TimeUtils.printTime(date, TimeUtils.TIME_PATTERN);
        System.out.println();
        TimeUtils.printTime(date, TimeUtils.DATE_PATTERN);
        System.out.println();
        TimeUtils.printTime(date, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        TimeUtils.printTime(date, TimeUtils.DATE_DETAIL_PATTERN);
        System.out.println();
        TimeUtils.printTime(date.getTime(), TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        System.out.println(TimeUtils.getSeason(date) + "季");
    }

    @Test
    public void testParseTime() throws ParseException {
        String dateTime1 = "2022-09-19";
        String dateTime2 = "2022-09-19 15:43:09:224";
        String dateTime3 = "公元 2022-09-19 03:43:09:224 星期一 262 39 4 下午 15 3 CST";
        Date date1 = TimeUtils.parseTime(dateTime1, TimeUtils.DATE_PATTERN);
        // 2022-09-19 00:00:00    可见，默认是零点零分零秒的时间
        TimeUtils.printTime(date1, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        // 2022-09-19 15:43:09
        Date date2 = TimeUtils.parseTime(dateTime2, TimeUtils.DATE_TIME_PATTERN);
        TimeUtils.printTime(date2, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        // 2022-09-19 15:43:09
        Date date3 = TimeUtils.parseTime(dateTime3, TimeUtils.DATE_DETAIL_PATTERN);
        TimeUtils.printTime(date3, TimeUtils.DATE_TIME_PATTERN);
    }

    @Test
    public void testGetTime() {
        Date date = new Date();
        Date timeOfJanuary1stZeroTime = TimeUtils.getTimeOfJanuary1stZeroTime(date);
        TimeUtils.printTime(timeOfJanuary1stZeroTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date lastTimeOfYear = TimeUtils.getLastTimeOfYear(date);
        TimeUtils.printTime(lastTimeOfYear, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date lastMonth1stZeroTime = TimeUtils.getLastMonth1stZeroTime(date);
        TimeUtils.printTime(lastMonth1stZeroTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date lastMonthLastTime = TimeUtils.getLastMonthLastTime(date);
        TimeUtils.printTime(lastMonthLastTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date yesterdayZeroTime = TimeUtils.getYesterdayZeroTime(date);
        TimeUtils.printTime(yesterdayZeroTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date yesterdayLastTime = TimeUtils.getYesterdayLastTime(date);
        TimeUtils.printTime(yesterdayLastTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date theMonth1stZeroTime = TimeUtils.getTheMonth1stZeroTime(date);
        TimeUtils.printTime(theMonth1stZeroTime, TimeUtils.DATE_TIME_PATTERN);
        System.out.println();
        Date theMonthLastTime = TimeUtils.getTheMonthLastTime(date);
        TimeUtils.printTime(theMonthLastTime, TimeUtils.DATE_TIME_PATTERN);
    }

    @Test
    public void testGetDayInterval() {
        Date begin = new Date(0);
        Date end = new Date(1000L * 60 * 60 * 24 * 30 * 25 - 1000L * 60 * 60 * 24 * 21);
        System.out.println(TimeUtils.getDayInterval(end, begin));
        System.out.println(TimeUtils.getDayInterval(begin, end));
        TimeUtils.printInterval(begin, end);
    }

    @Test
    public void testTimeAdd() {
        Date date = new Date();
        TimeUtils.printTime(date, TimeUtils.DATE_PATTERN);
        System.out.println();
        TimeUtils.printTime(TimeUtils.addDays(date, 1), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addDays(date, -1), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addDays(date, 12), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addDays(date, -19), TimeUtils.DATE_PATTERN);
        System.out.println();
        TimeUtils.printTime(TimeUtils.addMonths(date, 1), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addMonths(date, -1), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addMonths(date, 4), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addMonths(date, -9), TimeUtils.DATE_PATTERN);
        System.out.println();
        TimeUtils.printTime(TimeUtils.addYears(date, 1), TimeUtils.DATE_PATTERN);
        TimeUtils.printTime(TimeUtils.addYears(date, -1), TimeUtils.DATE_PATTERN);
    }

    /**
     * Java8 新特性展示，dateTimeFormatter是线程安全的类
     */
    @Test
    public void testJdk8Time() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now(ZoneId.of("America/New_York"));
        System.out.println(dateTimeFormatter.format(localDateTime));
        System.out.println(dateTimeFormatter.format(localDateTime1));

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(dateTimeFormatter.format(zonedDateTime));
        ZonedDateTime zonedDateTime1 = zonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        System.out.println(dateTimeFormatter.format(zonedDateTime1));
    }

    @Test
    void printTimeIntervalJava8() {
        Date date = new Date();
        Date date1 = TimeUtils.addDays(date, 365);
        TimeUtils.printTimeIntervalJava8(date, date1);
    }
}