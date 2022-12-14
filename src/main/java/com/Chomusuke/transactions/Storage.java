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

package com.chomusuke.transactions;

import java.util.*;

import com.chomusuke.listDecorators.NumberedOrderedArray;
import com.chomusuke.listDecorators.OrderedArray;
import com.chomusuke.util.DateUtil;
import com.chomusuke.util.Preconditions;

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
        transactions = new OrderedArray<NumberedOrderedArray<Transaction>>(new ArrayList<>(MEM_CACHE_CAPACITY));
    }

    /**
     * Initializes a Storage instance with pre-existing transaction data.
     * Mainly useful for testing.
     * 
     * @param transactions
     *                     a transactions MemCache
     */
    public Storage(OrderedArray<NumberedOrderedArray<Transaction>> transactions) {
        this.transactions = transactions;
    }

    /**
     * Adds transaction {@code t} to the MemCache
     * 
     * @param t
     *          a {@code Transaction}
     */
    public void addTransaction(Transaction t) {
        // Finds the Month array to which the transaction must be added
        int index = indexOfNumber(transactions, DateUtil.dateID(t.date()));
        if (index >= 0)
            transactions.get(index)
                    .add(t);
        else {
            // If no such array exists, create one
            // TODO: implement disk storage
            NumberedOrderedArray<Transaction> newArray = new NumberedOrderedArray<>(DateUtil.dateID(t.date()));
            newArray.add(t);
            transactions.add(newArray);
        }
    }

    /**
     * Removes the {@code Transaction} t if it is in the MemCache.
     * 
     * @param t
     *          a {@code Transaction}
     */
    public void removeTransaction(Transaction t) {
        /*
         * Uses Binary Search to find a transaction with the same date as t.
         * Since the transactions are ordered by date, and there may be several
         * transactions with the same date, binary search is likely to fall in a suite
         * of transactions with the same date.
         */
        NumberedOrderedArray<Transaction> mArray = readMonth(DateUtil.dateID(t.date()));
        int i = Collections.binarySearch(mArray, t);

        /*
         * If a Transaction with the same date as t is found,
         * the algorithm looks into the TXs on both sides of the transaction to find one
         * corresponding to t.
         * If such a transaction is found, it is removed from the array
         */
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
        // Only values with the dateID format are accepted
        Preconditions.checkArgument(DateUtil.isDateID(dateID));

        int index = Collections.binarySearch(transactions, new NumberedOrderedArray<>(null, dateID));
        if (index >= 0)
            return transactions.get(index);
        else
            return new NumberedOrderedArray<Transaction>(dateID);
    }

    /**
     * If a {@code NumberedOrderedArray} with the given number is found,
     * its index will be returned.
     * 
     * @param <E>
     *              The type of object contained in the list. Not relevant to this
     *              method.
     * @param array
     *              An array containing {@code NumberedOrderedArray}s
     * @param n
     *              the number to be searched
     * 
     * @return the index of a corresponding {@code NumberedOrderedArray} if found,
     *         -1 if not found
     */
    private static <E extends Comparable<E>> int indexOfNumber(List<NumberedOrderedArray<E>> array, int n) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getNumber() == n)
                return i;
        }

        return -1;
    }
}