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

package com.chomusuke.gui.element.tmp;

import com.chomusuke.logic.Transaction;
import javafx.scene.paint.Color;

/**
 * Provides a JavaFX Node that represents a transaction.
 */
public class TransactionTile extends Tile {

    Transaction baseTransaction;

    public TransactionTile(Transaction t, float value) {
        super(t.name(), value);

        baseTransaction = t;
        ColorTag colorTag = new ColorTag();
        getChildren().add(0, colorTag);



        // ----- STYLE -----
        {
            // Color-coded based on transaction type
            switch (t.transactionType()) {
                case REVENUE -> colorTag.setFill(Color.web("#33CC33"));  // Green
                case BUDGET -> colorTag.setFill(Color.web("#FFE066"));  // Yellow
                case BILL -> colorTag.setFill(Color.web("#FF1A75"));  // Red
                case SAVINGS -> colorTag.setFill(Color.web("#33CCFF"));  // Blue
            }
        }
    }

    /**
     * Returns the transaction used to build the tile.
     *
     * @return a {@code Transaction}
     */
    public Transaction getBaseTransaction() {

        return baseTransaction;
    }
}
