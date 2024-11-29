package org.gpd.dicenetwork.utils;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final String getNowTimeString(){
        Date date = new Date();
        DateFormat dateformat = new SimpleDateFormat("YYYYMMDD hh:mm:ss");
        return dateformat.format(date);
    }

    public static final String getAfterNowDateString(int afterMinute){
        Date afterDate = new Date(new Date().getTime() + 60000 * afterMinute);
        DateFormat dateformat = new SimpleDateFormat("YYYYMMDD hh:mm:ss");
        return dateformat.format(afterDate);
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public static String LocalDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public static String LocalDateTimeToString2(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dateTime.format(formatter);
    }

    /**
     * 计算下一个整十分的时间，并返回 LocalDateTime 对象。
     *
     * @return 下一个整十分的时间
     */
    public static LocalDateTime getNextTenMinuteDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int currentMinute = now.getMinute();
        int nextTenMinute = ((currentMinute / 10) + 1) * 10;

        // 如果已经是整十分，则跳到下一个整十分
//        if (currentMinute % 10 == 0) {
//            nextTenMinute += 10;
//        }

        // 如果超过 60 分钟，则跳到下个小时
        if (nextTenMinute >= 60) {
            nextTenMinute -= 60;
            now = now.plusHours(1);
        }

        return now.withMinute(nextTenMinute).withSecond(0).withNano(0);
    }

    /**
     * 计算下一个整十分的时间，并返回字符串格式。
     *
     * @return 下一个整十分的时间字符串
     */
    public static String getNextTenMinuteString() {
        LocalDateTime nextTenMinuteDateTime = getNextTenMinuteDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        return nextTenMinuteDateTime.format(formatter);
    }

    /**
     * 比较两个 LocalDateTime 类型的时间的早晚。
     *
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 如果 time1 在 time2 之前返回 -1，如果相等返回 0，如果在之后返回 1
     */
    public static int compareLocalDateTime(LocalDateTime time1, LocalDateTime time2) {
        return time1.compareTo(time2);
    }

    /**
     * 比较两个 String 类型的时间的早晚。
     *
     * @param time1 第一个时间
     * @param time2 第二个时间
     * @return 如果 time1 在 time2 之前返回 -1，如果相等返回 0，如果在之后返回 1
     * @throws IllegalArgumentException 如果输入的时间格式不正确
     */
    public static int compareTimeStrings(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

        LocalDateTime t1, t2;
        try {
            t1 = LocalDateTime.parse(time1, formatter);
            t2 = LocalDateTime.parse(time2, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format", e);
        }

        return t1.compareTo(t2);
    }
}
