package ru.samara.mapapp.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {

    private DateUtils() {
    }

    public static String dateToString(GregorianCalendar calendar) {
        return calendar.get(Calendar.HOUR) + ":" + ((calendar.get(Calendar.MINUTE) < 10) ? "0" : "")
                + calendar.get(Calendar.MINUTE) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." +
                calendar.get(Calendar.YEAR);
    }

    public static String dateToString(long time) {
        return dateToString(timeToCalendar(time));
    }

    public static GregorianCalendar timeToCalendar(long time) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(1000 * time));
        return calendar;
    }
}
