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
 * Allows to check a boolean parameter.
 */
public final class Preconditions {

    /**
     * Don't let anyone instantiate this class.
     */
    private Preconditions() {
    }

    /**
     * Checks if the given boolean argument is correct
     *
     * @param conditions
     *                   The conditions on the arguments
     *
     * @throws IllegalArgumentException if the conditions are not met
     */
    public static void checkArgument(boolean conditions) throws IllegalArgumentException {
        if (!conditions)
            throw new IllegalArgumentException();
    }
}
