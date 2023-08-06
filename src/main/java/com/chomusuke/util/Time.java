/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

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
