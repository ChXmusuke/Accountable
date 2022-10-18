/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package util;

/**
 * Provides tools to pack and unpack dates in 32-bit vectors.
 */
public final class DateUtil {

    private static final int MAX_YEAR = (int) Math.pow(2, 23) - 1;
    private static final int LENGTH_DAY = 5;
    private static final int LENGTH_MONTH = 4;
    private static final int LENGTH_YEAR = 23;

    // TODO: Find a better method
    public static final int FIRST_YEAR = 2022;

    /**
     * Don't let anyone instantiate this class.
     */
    private DateUtil() {
    }

    /**
     * Packs a date into a 32-bit vector in 23-4-5 format.
     *
     * @param year
     *              a year
     * @param month
     *              a month
     * @param day
     *              a day
     * 
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
     * Extracts the day from a date in 23-4-5 format.
     *
     * @param date
     *             a date
     * 
     * @return the day contained in the vector
     */
    public static int extractDay(int date) {
        return Bits.extractUnsigned(date, 0, LENGTH_DAY);
    }

    /**
     * Extract the month from a date in 23-4-5 format.
     *
     * @param date
     *             a date
     * 
     * @return the month contained in the vector
     */
    public static int extractMonth(int date) {
        return Bits.extractUnsigned(date, LENGTH_DAY, LENGTH_MONTH);
    }

    /**
     * Extracts the year from a date in 23-4-5 format.
     *
     * @param date
     *             a date
     * 
     * @return the year contained in the vector
     */
    public static int extractYear(int date) {
        return Bits.extractUnsigned(date, LENGTH_DAY + LENGTH_MONTH, LENGTH_YEAR);
    }
}
