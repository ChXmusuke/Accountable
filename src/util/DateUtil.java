package util;

public class DateUtil {

    private static final int MAX_YEAR = (int) Math.pow(2, 23) - 1;
    private static final int LENGTH_DAY = 5;
    private static final int LENGTH_MONTH = 4;
    private static final int LENGTH_YEAR = 23;

    // TODO: Find a better method
    public static final int FIRST_YEAR = 2022;

    /**
     * Don't let anyone instantiate this class
     */
    private DateUtil() {
    }

    /**
     * Packs a date into a 32-bit vector.
     * Note: The highest possible year value is indeed comically large.
     *
     * @param year
     *              a year
     * @param month
     *              a month
     * @param day
     *              a day
     * @return the packed date
     */
    public static int packDate(int year, int month, int day) {
        CheckConditions.checkArgument(year >= 0 && year <= MAX_YEAR &&
                month > 0 && month <= 12 &&
                day > 0 && day <= 31);

        int m_year = (year << LENGTH_DAY + LENGTH_MONTH);
        short m_month = (short) (month << LENGTH_DAY);
        short m_day = (short) day;

        return (m_year | m_month | m_day);
    }

    /**
     * Extract the day from a date in short format
     *
     * @param date
     *             a date
     * @return the day contained in the vector
     */
    public static int extractDay(int date) {
        return Bits.extractUnsigned(date, 0, LENGTH_DAY);
    }

    /**
     * Extract the month from a date in short format
     *
     * @param date
     *             a date
     * @return the month contained in the vector
     */
    public static int extractMonth(int date) {
        return Bits.extractUnsigned(date, LENGTH_DAY, LENGTH_MONTH);
    }

    /**
     * Extract the year from a date in short format
     *
     * @param date
     *             a date
     * @return the year contained in the vector
     */
    public static int extractYear(int date) {
        return Bits.extractUnsigned(date, LENGTH_DAY + LENGTH_MONTH, LENGTH_YEAR);
    }
}
