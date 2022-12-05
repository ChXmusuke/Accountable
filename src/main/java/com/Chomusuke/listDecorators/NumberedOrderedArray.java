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

package com.chomusuke.listDecorators;

import java.util.List;

/**
 * Represents a numbered, ordered array.
 * 
 * Wraps either an existing {@code List} object,
 * or a new one if no parameter is given.
 */
public class NumberedOrderedArray<E extends Comparable<E>>
        extends OrderedArray<E>
        implements Comparable<NumberedOrderedArray<E>> {

    private int n;

    /**
     * Use this constructor to wrap an existing {@code List}.
     * 
     * @param l
     *          a {@code List}
     * @param n
     *          a number
     */
    public NumberedOrderedArray(List<E> l, int n) {
        super(l);
        this.n = n;
    }

    /**
     * Use this constructor if you need a new array.
     * 
     * @param n
     *          a number
     */
    public NumberedOrderedArray(int n) {
        super();
        this.n = n;
    }

    /**
     * @return the number assigned to the array.
     */
    public int getNumber() {
        return n;
    }

    /**
     * Modifies the number assigned to the array.
     * 
     * @param n
     *          a number
     */
    public void setNumber(int n) {
        this.n = n;
    }

    @Override
    public int compareTo(NumberedOrderedArray<E> that) {
        return Integer.compare(this.n, that.n);
    }
}
