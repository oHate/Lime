package dev.ohate.lime.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

public final class TimeUtil {

    private static final long MILLS_IN_SECOND = 1000L;
    private static final long MILLS_IN_MINUTE = MILLS_IN_SECOND * 60L;
    private static final long MILLS_IN_HOUR = MILLS_IN_MINUTE * 60L;
    private static final long MILLS_IN_DAY = MILLS_IN_HOUR * 24L;
    private static final long MILLS_IN_WEEK = MILLS_IN_DAY * 7L;
    private static final long MILLS_IN_MONTH = MILLS_IN_DAY * 30L;
    private static final long MILLS_IN_YEAR = MILLS_IN_MONTH * 12;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm a zzz");

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
    }

    public static String dateToString(Instant date) {
        return DATE_FORMAT.format(date);
    }

    public static String dateToString(long milliseconds) {
        return dateToString(new Date(milliseconds));
    }

    public static String dateToString() {
        return dateToString(new Date());
    }

    public static String millisToTime(long millis) {
        if (millis < MILLS_IN_SECOND) {
            return "0s";
        }

        long years = millis / MILLS_IN_YEAR;
        millis -= years * MILLS_IN_YEAR;

        long months = millis / MILLS_IN_MONTH;
        millis -= months * MILLS_IN_MONTH;

        long weeks = millis / MILLS_IN_WEEK;
        millis -= weeks * MILLS_IN_WEEK;

        long days = millis / MILLS_IN_DAY;
        millis -= days * MILLS_IN_DAY;

        long hours = millis / MILLS_IN_HOUR;
        millis -= hours * MILLS_IN_HOUR;

        long minutes = millis / MILLS_IN_MINUTE;
        millis -= minutes * MILLS_IN_MINUTE;

        long seconds = millis / MILLS_IN_SECOND;

        var builder = new StringBuilder();

        if (years > 0) builder.append(years).append("y ");
        if (months > 0) builder.append(months).append("M ");
        if (weeks > 0) builder.append(weeks).append("w ");
        if (days > 0) builder.append(days).append("d ");
        if (hours > 0) builder.append(hours).append("h ");
        if (minutes > 0) builder.append(minutes).append("m ");
        if (seconds > 0) builder.append(seconds).append("s");

        return builder.toString().trim();
    }

    public static String millisToTimer(long millis) {
        Duration duration = Duration.ofMillis(millis);

        long days = duration.toDaysPart();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (days > 0) {
            return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
