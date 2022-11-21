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

package listDecorators;

import java.util.*;

/**
 * Represents a numbered, ordered array.
 */
public class OrderedArray<E extends Comparable<E>>
        extends WrappedList<E> {

    public OrderedArray(List<E> l) {
        super(l);
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
        if (e == null)
            throw new NullPointerException();

        for (int i = super.size(); i > 0; i--) {
            if (super.get(i - 1).compareTo(e) <= 0) {
                super.add(i, e);
                return true;
            }
        }
        super.add(0, e);

        return true;
    }
}
