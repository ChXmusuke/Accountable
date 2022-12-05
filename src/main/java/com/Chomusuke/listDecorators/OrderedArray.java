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
 * Represents an ordered array.
 * 
 * Wraps either an existing {@code List} object,
 * or a new one if no parameter is given.
 */
public class OrderedArray<E extends Comparable<E>>
        extends WrappedList<E> {

    /**
     * Use this constructor to wrap an existing {@code List}.
     * 
     * @param l
     *          a list
     */
    public OrderedArray(List<E> l) {
        super(l);
    }

    /**
     * Constructor that does not require a List as argument.
     */
    public OrderedArray() {
        super();
    }

    /**
     * Inserts the specified element at the right index for it to be in order inside
     * the list.
     *
     * @param e element to be added to this list
     * 
     * @return {@code true} (as specified by {@link Collection#add})
     * 
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(E e) {
        // This class is not adequate for storage of null objects.
        if (e == null)
            throw new NullPointerException();

        /*
         * Inserts e at the right index in the list.
         * 
         * This class is made in the context of adding elements
         * which are likely to be closer to the end of the array.
         * For this reason, this method searches linearly the correct insertion point
         * from the last element.
         */
        for (int i = super.size(); i > 0; i--) {
            if (super.get(i - 1).compareTo(e) <= 0) {
                super.add(i, e);
                return true;
            }
        }
        // Inserts at the beginning if the loop is exited
        // without having inserted the element successfully
        super.add(0, e);

        return true;
    }
}
