package ru.samara.mapapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtils {

    private DateUtils() {
    }

    public static String dateToString(GregorianCalendar calendar) {
        return printZero(calendar.get(Calendar.HOUR_OF_DAY))
                + ":" +
                printZero(calendar.get(Calendar.MINUTE))
                + " " +
                printZero(calendar.get(Calendar.DAY_OF_MONTH))
                + "." +
                printZero(calendar.get(Calendar.MONTH))
                + "." +
                calendar.get(Calendar.YEAR);
    }

    private static String printZero(int x) {
        return ((x < 10) ? "0" : "") + x;
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
