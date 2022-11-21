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

import listDecorators.NumberedOrderedArray;
import listDecorators.OrderedArray;
import transactions.Transaction;
import util.CheckConditions;
import util.DateUtil;

public class Storage {

    final private static int MEM_CACHE_CAPACITY = 36;

    // Memory cache
    // Array structure: Memory<years<months>>
    final private OrderedArray<NumberedOrderedArray<Transaction>> transactions;

    /**
     * Constructor for a storage structure.
     * 
     * Contains an array for the memory cache.
     */
    public Storage() {
        transactions = new OrderedArray<>(new ArrayList<>(MEM_CACHE_CAPACITY));
    }

    public Storage(OrderedArray<NumberedOrderedArray<Transaction>> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction t) {
        int index = indexOfNumber(transactions, DateUtil.dateID(t.date()));
        if (index >= 0)
            transactions.get(index)
                    .add(t);
        else {
            NumberedOrderedArray<Transaction> newArray = new NumberedOrderedArray<>(new ArrayList<>(),
                    DateUtil.dateID(t.date()));
            transactions.add(newArray);
        }
    }

    public void removeTransaction(Transaction t) {
        NumberedOrderedArray<Transaction> mArray = readMonth(DateUtil.dateID(t.date()));
        int i = Collections.binarySearch(mArray, t);

        if (i >= 0) {
            for (int d = 0; i + d < mArray.size() && mArray.get(i + d).date() == t.date(); d++)
                if (mArray.get(i + d).equals(t))
                    mArray.remove(i + d);
            for (int d = 1; i - d >= 0 && mArray.get(i - d).date() == t.date(); d++) {
                if (mArray.get(i - d).equals(t))
                    mArray.remove(i - d);
            }
        }
    }

    /**
     * Queries the given month's data from memory cache.
     * 
     * @param dateID
     *               a dateID
     * 
     * @return A {@code NumberedArray} with the month's transaction data.
     *         the {@code NumberedArray} is empty if the data has not been
     *         found.
     */
    public NumberedOrderedArray<Transaction> readMonth(int dateID) {
        CheckConditions.checkArgument(DateUtil.isDateID(dateID));

        int index = Collections.binarySearch(transactions, new NumberedOrderedArray<>(null, dateID));
        if (index >= 0)
            return transactions.get(index);
        else
            return new NumberedOrderedArray<>(new ArrayList<Transaction>(), dateID);
    }

    private static <E extends Comparable<E>> int indexOfNumber(List<NumberedOrderedArray<E>> array, int n) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getNumber() == n)
                return i;
        }

        return -1;
    }
}