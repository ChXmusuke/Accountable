package util;

/**
 * This class provides methods to work on bit vectors.
 */
public class Bits {

    /**
     * Don't let anyone instantiate this class
     */
    private Bits() {}

    /**
     * Trims vector from "from" bit to "to" bit,
     * from right to left
     *
     * @param vector
     *        A 16-bit vector
     * @param from
     *        The first bit from the right
     * @param to
     *        The last bit from the right
     * @return
     *        The trimmed 16-bit vector
     */
    public static short extract(short vector, int from, int to) {
        CheckConditions.checkArgument(from <= to && from > 0 && to < 16);

        int leftShift = Short.SIZE-1-to;
        int rightShift = leftShift + from;

        return (short) ((vector << leftShift) >>> rightShift);
    }

    /**
     * Extract the day from a date in short format
     *
     * @param  date
     *         a date
     * @return the day contained in the vector
     */
    public static short extractDay(short date) {
        return extract(date, 0, 4);
    }

    /**
     * Extract the month from a date in short format
     *
     * @param  date
     *         a date
     * @return the month contained in the vector
     */
    public static short extractMonth(short date) {
        return extract(date, 5, 8);
    }

    /**
     * Extract the year from a date in short format
     *
     * @param  date
     *         a date
     * @return the year contained in the vector
     */
    public static short extractYear(short date) {
        return extract(date, 9, 14);
    }

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
        CheckConditions.checkArgument(year > 0 && year <= 99 &&
                month > 0 && month <= 12 &&
                day > 0 && day <= 31);

        short m_year = (short) (year << 9);
        short m_month = (short) (month << 5);
        short m_day = (short) day;

        return (short) (m_year | m_month | m_day);
    }
}