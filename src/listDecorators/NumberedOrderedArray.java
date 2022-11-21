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
public class NumberedOrderedArray<E extends Comparable<E>>
        extends OrderedArray<E>
        implements Comparable<NumberedOrderedArray<E>> {

    private int n;

    public NumberedOrderedArray(List<E> l, int n) {
        super(l);
        this.n = n;
    }

    public int getNumber() {
        return n;
    }

    public void setNumber(int n) {
        this.n = n;
    }

    @Override
    public int compareTo(NumberedOrderedArray<E> that) {
        return Integer.compare(this.n, that.n);
    }
}
