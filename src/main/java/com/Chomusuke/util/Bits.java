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

package com.chomusuke.util;

/**
 * Provides methods to work on bit vectors.
 */
public final class Bits {

    /**
     * Don't let anyone instantiate this class.
     */
    private Bits() {
    }

    /**
     * Extracts a vector of given length from a 16-bit short
     *
     * @param vector
     *               A 32-bit vector
     * @param start
     *               The first bit of the extracted vector
     * @param length
     *               The length of the extracted vector
     * 
     * @return The trimmed 32-bit vector
     */
    public static int extractUnsigned(int vector, int start, int length) {
        Preconditions.checkArgument(validExtraction(start, length));

        vector = vector << Integer.SIZE - (start + length);
        vector = vector >>> Integer.SIZE - length;

        return vector;
    }

    /**
     * Helper method used to check the validity of a bit extraction.
     *
     * @param start
     *               The start of the bit vector
     * @param length
     *               The length of the bit vector
     * @return Validity of the extraction (bool)
     */
    private static boolean validExtraction(int start, int length) {

        return 0 <= start && start + length <= Integer.SIZE && length >= 0 && length < Integer.SIZE;
    }
}