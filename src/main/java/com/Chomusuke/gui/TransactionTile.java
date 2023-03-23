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

package com.chomusuke.gui;

import com.chomusuke.transactions.Transaction;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TransactionTile extends HBox {

    private static final String TEXT_COLOR = "-fx-fill: lightgrey";

    public TransactionTile(Transaction t, float value) {
        setPadding(new Insets(8));
        setSpacing(8);

        Rectangle colorTag = new Rectangle();
        colorTag.getStyleClass().add("colorTag");
        colorTag.heightProperty().bind(this.heightProperty().multiply(0.8));
        colorTag.setWidth(8);

        switch (t.transactionType()) {
            case REVENUE -> colorTag.setFill(Color.web("#33CC33"));
            case BUDGET -> colorTag.setFill(Color.web("#FFE066"));
            case BILL -> colorTag.setFill(Color.web("#FF1A75"));
            case SAVINGS -> colorTag.setFill(Color.web("#33CCFF"));
        }

        Text nameText = new Text(t.name());
        Text valueText = new Text(String.format("%.2f", value));
        nameText.setStyle(TEXT_COLOR);
        valueText.setStyle(TEXT_COLOR);

        VBox textBox = new VBox(nameText, valueText);
        textBox.setPadding(new Insets(8));
        textBox.setSpacing(8);

        textBox.getChildren().forEach(c -> ((Text) c).setFont(new Font("Helvetica", 24)));

        this.getStyleClass().add("tile");
        this.getChildren().addAll(colorTag, textBox);
    }
}
