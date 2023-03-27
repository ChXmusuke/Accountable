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


import com.chomusuke.gui.element.TransactionTile;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static com.chomusuke.logic.Transaction.TransactionType;

public class TransactionList {

    private final ObservableList<Transaction> txs = FXCollections.observableArrayList();
    private int nextRevenueIndex = 0;

    public void setTransactionList(List<Transaction> txs) {
        this.txs.setAll(txs);
    }

    public float[] getValues() {
        float[] values = new float[txs.size()];
        float valueP = 0;
        float valueN = 0;

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
     * Replaces {@code oldT} with {@code t}.
     *
     * @param t The new transaction
     * @param oldT The old transaction
     */
    public void add(Transaction t, Transaction oldT) {
        if (oldT != null) {
            int index = txs.indexOf(oldT);
            txs.set(index, t);
        } else {
            txs.add(t);
        }
    }

    public void add(Transaction t) {
        add(t, null);
        if (t != null) {
            if (t.transactionType().equals(TransactionType.REVENUE))
                this.txs.add(nextRevenueIndex++, t);
            else this.txs.add(t);
        }
    }

    public void remove(Transaction t) {
        remove(txs.indexOf(t));
    }

    public void remove(int index) {
        try {
            if (txs.remove(index).transactionType() == TransactionType.REVENUE)
                nextRevenueIndex--;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReadOnlyListProperty<Transaction> getTransactionsProperty() {
        return new ReadOnlyListWrapper<>(txs);
    }

    public List<TransactionTile> getTiles() {
        List<TransactionTile> tiles = new ArrayList<>();
        for (int i = 0 ; i < txs.size() ; i++) {
            tiles.add(new TransactionTile(txs.get(i), getValues()[i]));
        }

        return tiles;
    }
}
