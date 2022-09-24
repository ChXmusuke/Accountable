package util;

public class Date {

    /**
     * Don't let anyone instantiate this class
     */
    private Date() {}

    /**
     * Packs a date into a 16-bit vector.
     *
     * @param  year
     *         a year (2 last digits)
     * @param  month
     *         a month
     * @param  day
     *         a day
     * @return the packed date
     */
    public static short packDate(int year, int month, int day) {
        CheckConditions.checkArgument(year >= 0 && year <= 99 &&
                month > 0 && month <= 12 &&
                day > 0 && day <= 31);

        short m_year = (short) (year << 9);
        short m_month = (short) (month << 5);
        short m_day = (short) day;

        return (short) (m_year | m_month | m_day);
    }

    /**
     * Extract the day from a date in short format
     *
     * @param  date
     *         a date
     * @return the day contained in the vector
     */
    public static short extractDay(short date) {
        return Bits.extractUnsigned(date, 0, 5);
    }

    /**
     * Extract the month from a date in short format
     *
     * @param  date
     *         a date
     * @return the month contained in the vector
     */
    public static short extractMonth(short date) {
        return Bits.extractUnsigned(date, 5, 4);
    }

    /**
     * Extract the year from a date in short format
     *
     * @param  date
     *         a date
     * @return the year contained in the vector
     */
    public static short extractYear(short date) {
        return Bits.extractUnsigned(date, 9, 7);
    }
}
