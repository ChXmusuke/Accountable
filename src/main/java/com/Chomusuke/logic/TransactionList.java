/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

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

package com.chomusuke.logic;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static com.chomusuke.logic.Transaction.TransactionType;

/**
 * This class provides memory storage of transactions.
 * <br>
 * Uses the default constructor.
 */
public class TransactionList {

    private final ObservableList<Transaction> txs = FXCollections.observableArrayList();
    private final ObservableList<Transaction> unmodifiableTxs = FXCollections.unmodifiableObservableList(txs);

    private boolean setAllFlag = false;

    /**
     * Replaces all transactions in memory by the transactions
     * in {@code txs}.
     *
     * @param txs a {@code List} of {@code Transaction} objects
     */
    public void setTransactionList(List<Transaction> txs) {

        // A flag is necessary for listeners to make a difference
        // between individual operations and full list replacements

        setAllFlag = true;

        this.txs.setAll(txs);

        setAllFlag = false;
    }

    /**
     * Gets the status of the {@code setAllFlag}.
     *
     * @return {@code true} if the change being witnessed
     *         is a {@code setAll} operation,
     *         {@code false} if not.
     */
    public boolean setAllFlag() {

        return setAllFlag;
    }

    /**
     * Computes the values of all transactions
     * currently in memory.
     *
     * @return an array of float values
     */
    public float[] getValues() {
        float[] values = new float[txs.size()];
        float valueP = 0;  // Total income
        float valueN = 0;  // Total expenses

        for (int i = 0 ; i < txs.size() ; i++) {
            Transaction t = txs.get(i);
            values[i] = t.value(valueP, valueN);

            if (t.transactionType().equals(TransactionType.REVENUE)) {
                valueP += values[i];
            } else {
                valueN += values[i];
                values[i] *= -1;
            }
        }

        return values;
    }

    /**
     * Returns the sum of all transactions
     * of {@code TransactionType.REVENUE}.
     *
     * @return the total {@code float} value
     */
    public float getTotalRevenue() {
        float s = 0;

        for (Transaction t : txs) {
            if (t.transactionType() != TransactionType.REVENUE)
                break;

            s += t.value();
        }

        return s;
    }

    public float getRemainder() {
        return getRemainder(getValues());
    }

    public float getRemainder(float[] values) {
        float sum = 0;

        for (float v : values) {
            sum += v;
        }

        return Math.round(sum * 100)/100.0f;
    }

    /**
     * Adds transaction {@code t} to the list.
     *
     * @param t A new transaction
     */
    public void add(Transaction t) {

        add(t, null);
    }

    /**
     * Adds transaction {@code t} to the list.
     * Replaces {@code oldT} with {@code t} if
     * provided an {@code oldT} to replace.
     *
     * @param t The new transaction
     * @param oldT The old transaction
     */
    public void add(Transaction t, Transaction oldT) {

        if (oldT != null) {

            int index = txs.indexOf(oldT);
            txs.set(index, t);
        } else {

            if (t.transactionType().equals(TransactionType.REVENUE))
                txs.add(0, t);
            else txs.add(t);
        }
    }

    /**
     * Removes the transaction {@code t} from the list.
     *
     * @param t A transaction
     */
    public void remove(Transaction t) {

        txs.remove(t);
    }

    /**
     * Returns an unmodifiable version of the internal
     * {@code Transaction} list.
     *
     * @return an unmodifiable list
     */
    public ObservableList<Transaction> getTransactionList() {

        return unmodifiableTxs;
    }
}
