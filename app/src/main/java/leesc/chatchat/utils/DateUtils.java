package leesc.chatchat.utils;

import android.text.format.Time;

import java.text.SimpleDateFormat;

public class DateUtils {

    private static final String FORMAT_TIME = "h:mm";
    private static final String FORMAT_MONTH_DAY = "M월 d일";
    private static final String FORMAT_YEAR_MONTH_DAY = "yyyy년 M월 d일";
    private static final String FORMAT_AM_PM = "a";
    private static final String FORMAT_DAY_OF_WEEK = "EEEE";
    private static final String FORMAT_TODAY = "오늘";
    private static final String FORMAT_YESTERDATY = "어제";
    private static final String FORMAT_DAY_WEEK = "d일 E요일";

    public static String formatReserveDate(final long millis) {
        Time current = new Time();
        current.set(System.currentTimeMillis());

        Time time = new Time();
        time.set(millis);

        final String pattern;
        if (current.year == time.year) {
            pattern = FORMAT_MONTH_DAY + " " + FORMAT_AM_PM + " " + FORMAT_TIME;
        } else {
            pattern = FORMAT_YEAR_MONTH_DAY + " " + FORMAT_AM_PM + " " + FORMAT_TIME;
        }
        return new SimpleDateFormat(pattern).format(millis);
    }

    public static CharSequence formatReserveOrSpamDetailDate(final long millis) {
        Time current = new Time();
        current.set(System.currentTimeMillis());

        Time time = new Time();
        time.set(millis);

        String pattern = FORMAT_YEAR_MONTH_DAY + " " + FORMAT_AM_PM + " " + FORMAT_TIME;
        return new SimpleDateFormat(pattern).format(millis);
    }

    public static String formatListDate(final long millis) {
        Time current = new Time();
        current.set(System.currentTimeMillis());

        Time time = new Time();
        time.set(millis);

        final String pattern;
        if (current.year == time.year) {
            if (current.month == time.month && current.monthDay == time.monthDay) {
                pattern = FORMAT_AM_PM + " " + FORMAT_TIME;
            } else {
                pattern = FORMAT_MONTH_DAY;
            }
        } else {
            pattern = FORMAT_YEAR_MONTH_DAY;
        }
        return new SimpleDateFormat(pattern).format(millis);
    }

    public static CharSequence formatInfoMessageListDate(final long millis) {
        Time current = new Time();
        current.set(System.currentTimeMillis());

        Time time = new Time();
        time.set(millis);

        final String pattern;
        if (current.year == time.year) {
            pattern = FORMAT_MONTH_DAY;
        } else {
            pattern = FORMAT_YEAR_MONTH_DAY;
        }
        return new SimpleDateFormat(pattern).format(millis);
    }

    public static CharSequence formatMessagesDate(final long millis) {
        Time current = new Time();
        current.set(System.currentTimeMillis());

        Time time = new Time();
        time.set(millis);

        String pattern = FORMAT_YEAR_MONTH_DAY + " " + FORMAT_DAY_OF_WEEK;
        return new SimpleDateFormat(pattern).format(millis);
    }

    public static boolean isSameDay(long firstMillis, long secondMillis) {
        Time firstTime = new Time();
        firstTime.set(firstMillis);

        Time secondTime = new Time();
        secondTime.set(secondMillis);

        if (firstTime.year == secondTime.year && firstTime.month == secondTime.month
            && firstTime.monthDay == secondTime.monthDay) {
            return true;
        }
        return false;
        
    }

    public static String formatCallLogDate(final long millis) {

        Time day = new Time();
        day.set(millis);

        Time nextDay = new Time();
        nextDay.set(millis + (1000 * 60 * 60 * 24));

        Time today = new Time();
        today.set(System.currentTimeMillis());

        if (today.year == day.year && today.month == day.month && today.monthDay == day.monthDay) {
            return FORMAT_TODAY;
        } else if (today.year == nextDay.year && today.month == nextDay.month && today.monthDay == nextDay.monthDay) {
            return FORMAT_YESTERDATY;
        } else if (today.year == day.year) {
            return new SimpleDateFormat(FORMAT_MONTH_DAY).format(millis);
        } else {
            return new SimpleDateFormat(FORMAT_YEAR_MONTH_DAY).format(millis);
        }
    }

    public static String formatDate(long millis) {
        return new SimpleDateFormat(FORMAT_MONTH_DAY).format(millis);
    }

    public static String formatAmPm(long millis) {
        return new SimpleDateFormat(FORMAT_AM_PM).format(millis);
    }

    public static String formatTime(long millis) {
        return new SimpleDateFormat(FORMAT_TIME).format(millis);
    }

    public static String formatDayWeek(long millis) {
        return new SimpleDateFormat(FORMAT_DAY_WEEK).format(millis);
    }
}
