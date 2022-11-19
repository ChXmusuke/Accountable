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

    final private static int MEM_CACHE_CAPACITY = 3;

    // Memory cache
    // Array structure: Memory<years<months>>
    final private List<NumberedArray<NumberedArray<Transaction>>> transactions;

    /**
     * Constructor for a storage structure.
     * 
     * Contains an array for the memory cache.
     */
    public Storage() {
        transactions = new ArrayList<>(MEM_CACHE_CAPACITY);
    }

    public Storage(List<NumberedArray<NumberedArray<Transaction>>> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction t) {
        int year = DateUtil.extractYear(t.date());
        int month = DateUtil.extractMonth(t.date());

        NumberedArray<NumberedArray<Transaction>> yArray = transactions.get(indexOfNumber(transactions, year));
        NumberedArray<Transaction> mArray = yArray.get(indexOfNumber(yArray, month));
        insertTransaction(mArray, t);
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
    public NumberedArray<Transaction> readMonth(int year, int month) {
        for (int i = 0; i < MEM_CACHE_CAPACITY; i++) {
            NumberedArray<NumberedArray<Transaction>> y = transactions.get(i);
            if (y.getNumber() == year)
                for (int j = 0; j < transactions.get(i).size(); j++) {
                    NumberedArray<Transaction> m = transactions.get(i).get(j);
                    if (m.getNumber() == month)
                        return m;
                }
        }

        return new NumberedArray<>(new ArrayList<Transaction>(), month);
    }

    public int insertTransaction(NumberedArray<Transaction> array, Transaction t) {
        for (int i = array.size(); i > 0; i--) {
            if (array.get(i - 1).compareTo(t) <= 0) {
                array.add(i, t);
                return i;
            }
        }
        array.add(0, t);

        return 0;
    }

    private static <E> int indexOfNumber(List<NumberedArray<E>> array, int n) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getNumber() == n)
                return i;
        }

        return -1;
    }
}