package com.bujian.aipersnonknowledge.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils extends PropertyEditorSupport {
    public static ThreadLocal<SimpleDateFormat> date_sdf = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    public static ThreadLocal<SimpleDateFormat> yyyyMMdd = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    public static ThreadLocal<SimpleDateFormat> date_sdf_wz = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日");
        }
    };
    public static ThreadLocal<SimpleDateFormat> time_sdf = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }
    };
    public static ThreadLocal<SimpleDateFormat> yyyymmddhhmmss = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };
    public static ThreadLocal<SimpleDateFormat> short_time_sdf = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };
    public static ThreadLocal<SimpleDateFormat> datetimeFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private static final long DAY_IN_MILLIS = 86400000L;
    private static final long HOUR_IN_MILLIS = 3600000L;
    private static final long MINUTE_IN_MILLIS = 60000L;
    private static final long SECOND_IN_MILLIS = 1000L;

    public DateUtils() {
    }

    private static SimpleDateFormat getSdFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static Calendar getCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(millis));
        return cal;
    }

    public static Date getDate() {
        return new Date();
    }

    public static Date getDateDiff(int diffSec) {
        Calendar cal = Calendar.getInstance();
        cal.add(13, diffSec);
        return cal.getTime();
    }

    public static Date plusDay(Date currentDate, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(5, day);
        return cal.getTime();
    }

    public static Date plusHour(Date currentDate, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(10, hour);
        return cal.getTime();
    }

    public static Date plusMinute(Date currentDate, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(12, minute);
        return cal.getTime();
    }

    public static Date plusSecond(Date currentDate, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(13, second);
        return cal.getTime();
    }

    public static Date getDateDiff(Date currentDate, int diffSec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(13, diffSec);
        return cal.getTime();
    }

    public static Date getDate(long millis) {
        return new Date(millis);
    }

    public static String timestamptoStr(Timestamp time) {
        Date date = null;
        if (null != time) {
            new Date(time.getTime());
        }

        return date2Str((SimpleDateFormat)date_sdf.get());
    }

    public static Timestamp str2Timestamp(String str) {
        Date date = str2Date(str, (SimpleDateFormat)date_sdf.get());
        return new Timestamp(date.getTime());
    }

    public static Date str2Date(String str, SimpleDateFormat sdf) {
        if (null != str && !str.isEmpty()) {
            Date date = null;

            try {
                date = sdf.parse(str);
                return date;
            } catch (ParseException var4) {
                ParseException e = var4;
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date str2Date(String str, String sdf) {
        if (null != str && !str.isEmpty()) {
            try {
                return getSdFormat(sdf).parse(str);
            } catch (ParseException var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String date2Str(SimpleDateFormat dateSdf) {
        synchronized(dateSdf) {
            Date date = getDate();
            return null == date ? null : dateSdf.format(date);
        }
    }

    public static String dateformat(String date, String format) {
        SimpleDateFormat sformat = new SimpleDateFormat(format);
        Date nowDate = null;

        try {
            nowDate = sformat.parse(date);
        } catch (ParseException var5) {
            ParseException e = var5;
            e.printStackTrace();
        }

        return sformat.format(nowDate);
    }

    public static String date2Str(Date date, SimpleDateFormat dateSdf) {
        synchronized(dateSdf) {
            return null == date ? null : dateSdf.format(date);
        }
    }

    public static String getDate(String format) {
        Date date = new Date();
        if (null == date) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        }
    }

    public static Timestamp getTimestamp(long millis) {
        return new Timestamp(millis);
    }

    public static Timestamp getTimestamp(String time) {
        return new Timestamp(Long.parseLong(time));
    }

    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String now() {
        return ((SimpleDateFormat)datetimeFormat.get()).format(getCalendar().getTime());
    }

    public static Timestamp getTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    public static Timestamp getCalendarTimestamp(Calendar cal) {
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp gettimestamp() {
        Date dt = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = ((DateFormat)df).format(dt);
        Timestamp buydate = Timestamp.valueOf(nowTime);
        return buydate;
    }

    public static long getMillis() {
        return System.currentTimeMillis();
    }

    public static long getMillis(Calendar cal) {
        return cal.getTime().getTime();
    }

    public static long getMillis(Date date) {
        return date.getTime();
    }

    public static long getMillis(Timestamp ts) {
        return ts.getTime();
    }

    public static String formatDate() {
        return ((SimpleDateFormat)date_sdf.get()).format(getCalendar().getTime());
    }

    public static String formatDateTime() {
        return ((SimpleDateFormat)datetimeFormat.get()).format(getCalendar().getTime());
    }

    public static String getDataString(SimpleDateFormat formatstr) {
        synchronized(formatstr) {
            return formatstr.format(getCalendar().getTime());
        }
    }

    public static String formatDate(Calendar cal) {
        return ((SimpleDateFormat)date_sdf.get()).format(cal.getTime());
    }

    public static String formatDate(Date date) {
        return ((SimpleDateFormat)date_sdf.get()).format(date);
    }

    public static String formatDate(long millis) {
        return ((SimpleDateFormat)date_sdf.get()).format(new Date(millis));
    }

    public static String formatDate(String pattern) {
        return getSdFormat(pattern).format(getCalendar().getTime());
    }

    public static String formatDate(Calendar cal, String pattern) {
        return getSdFormat(pattern).format(cal.getTime());
    }

    public static String formatDate(Date date, String pattern) {
        return getSdFormat(pattern).format(date);
    }

    public static String formatTime() {
        return ((SimpleDateFormat)time_sdf.get()).format(getCalendar().getTime());
    }

    public static String formatTime(long millis) {
        return ((SimpleDateFormat)time_sdf.get()).format(new Date(millis));
    }

    public static String formatTime(Calendar cal) {
        return ((SimpleDateFormat)time_sdf.get()).format(cal.getTime());
    }

    public static String formatTime(Date date) {
        return ((SimpleDateFormat)time_sdf.get()).format(date);
    }

    public static String formatShortTime() {
        return ((SimpleDateFormat)short_time_sdf.get()).format(getCalendar().getTime());
    }

    public static String formatShortTime(long millis) {
        return ((SimpleDateFormat)short_time_sdf.get()).format(new Date(millis));
    }

    public static String formatShortTime(Calendar cal) {
        return ((SimpleDateFormat)short_time_sdf.get()).format(cal.getTime());
    }

    public static String formatShortTime(Date date) {
        return ((SimpleDateFormat)short_time_sdf.get()).format(date);
    }

    public static Date parseDate(String src, String pattern) throws ParseException {
        return getSdFormat(pattern).parse(src);
    }

    public static Calendar parseCalendar(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String formatAddDate(String src, String pattern, int amount) throws ParseException {
        Calendar cal = parseCalendar(src, pattern);
        cal.add(5, amount);
        return formatDate(cal);
    }

    public static Timestamp parseTimestamp(String src, String pattern) throws ParseException {
        Date date = parseDate(src, pattern);
        return new Timestamp(date.getTime());
    }

    public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {
        long millisDiff = getMillis(calSrc) - getMillis(calDes);
        char year = 121;
        char day = 100;
        char hour = 104;
        char minute = 109;
        char second = 115;
        if (flag == year) {
            return calSrc.get(1) - calDes.get(1);
        } else if (flag == day) {
            return (int)(millisDiff / 86400000L);
        } else if (flag == hour) {
            return (int)(millisDiff / 3600000L);
        } else if (flag == minute) {
            return (int)(millisDiff / 60000L);
        } else {
            return flag == second ? (int)(millisDiff / 1000L) : 0;
        }
    }

    public static Long getCurrentTimestamp() {
        return Long.valueOf(((SimpleDateFormat)yyyymmddhhmmss.get()).format(new Date()));
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            try {
                int length10 = 10;
                int length19 = 19;
                if (text.indexOf(":") == -1 && text.length() == length10) {
                    this.setValue(((SimpleDateFormat)date_sdf.get()).parse(text));
                } else {
                    if (text.indexOf(":") <= 0 || text.length() != length19) {
                        throw new IllegalArgumentException("Could not parse date, date format is error ");
                    }

                    this.setValue(((SimpleDateFormat)datetimeFormat.get()).parse(text));
                }
            } catch (ParseException var4) {
                ParseException ex = var4;
                IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            this.setValue((Object)null);
        }

    }

    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDate());
        return calendar.get(1);
    }
}
