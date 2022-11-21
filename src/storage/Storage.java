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

package storage;

import java.util.*;

import transactions.Transaction;
import util.DateUtil;

public class Storage {

    final private static int MEM_CACHE_CAPACITY = 36;

    // Memory cache
    // Array structure: Memory<years<months>>
    final private List<Norray<Transaction>> transactions;

    /**
     * Constructor for a storage structure.
     * 
     * Contains an array for the memory cache.
     */
    public Storage() {
        transactions = new ArrayList<>(MEM_CACHE_CAPACITY);
    }

    public Storage(List<Norray<Transaction>> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.get(indexOfNumber(transactions, DateUtil.dateID(t.date())))
                .add(t);
    }

    /**
     * Queries the given month's data from memory cache.
     * 
     * @param year
     *              a year
     * @param month
     *              a month
     * @return A {@code NumberedArray} with the month's transaction data.
     *         the {@code NumberedArray} is empty if the data has not been
     *         found.
     */
    public Norray<Transaction> readMonth(int year, int month) {
        int searchedDate = DateUtil.dateID(DateUtil.packDate(year, month, 0));

        int index = Collections.binarySearch(transactions, new Norray<>(null, searchedDate));
        if (index >= 0)
            return transactions.get(index);
        else
            return new Norray<>(new ArrayList<Transaction>(), searchedDate);
    }

    private static <E extends Comparable<E>> int indexOfNumber(List<Norray<E>> array, int n) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getNumber() == n)
                return i;
        }

        return -1;
    }
}