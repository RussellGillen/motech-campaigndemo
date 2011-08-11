package org.motechproject.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class DateUtil {
    public static DateTime now() {
        DateTimeZone timeZone = getTimeZone();
        return new DateTime(timeZone);
    }

    public static LocalDate today() {
        return new LocalDate(getTimeZone());
    }

    public static LocalDate newDate(int year, int month, int day) {
        return new LocalDate(getTimeZone()).withYear(year).withMonthOfYear(month).withDayOfMonth(day);
    }

    public static DateTime newDateTime(LocalDate localDate, int hour, int minute, int second) {
        return new DateTime(getTimeZone()).
                withYear(localDate.getYear()).withMonthOfYear(localDate.getMonthOfYear()).withDayOfMonth(localDate.getDayOfMonth())
                .withHourOfDay(hour).withMinuteOfHour(minute).withSecondOfMinute(second);
    }

    public static LocalDate newDate(Date date) {
        if (date == null) return null;
        return new LocalDate(date.getTime(), getTimeZone());
    }

    private static DateTimeZone getTimeZone() {
        String timeZoneString = ResourceBundle.getBundle("date").getString("timezone");
        return DateTimeZone.forTimeZone(TimeZone.getTimeZone(timeZoneString));
    }
}