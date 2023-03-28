package com.chomusuke.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time {
    private static final Calendar currentDate = GregorianCalendar.getInstance();

    /**
     * Don't let anyone instantiate this class.
     */
    private Time() {}

    /**
     * Returns the current year.
     *
     * @return the year during which the program is being run.
     */
    public static int getCurrentYear() {

        return currentDate.get(Calendar.YEAR);
    }

    /**
     * Returns the current month.
     *
     * @return the month during whifh the program is being run.
     */
    public static int getCurrentMonth() {

        return currentDate.get(Calendar.MONTH)+1;
    }
}
