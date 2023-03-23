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

package com.chomusuke;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.chomusuke.transactions.Transaction;
import com.chomusuke.transactions.TransactionList;

import java.util.List;

import static com.chomusuke.transactions.Transaction.TransactionType;
import static com.chomusuke.transactions.Transaction.ValueType;

public class Accountable extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setWidth(512);
        stage.setHeight(768);

        TransactionList manager = new TransactionList();

        VBox root = new VBox();
        root.getStyleClass().add("background");
        root.setSpacing(8);
        Scene scene = new Scene(root);

        VBox transactionPane = new VBox();
        transactionPane.getStyleClass().add("background");
        transactionPane.setPadding(new Insets(8));
        transactionPane.setSpacing(8);

        ScrollPane scrollPane = new ScrollPane(transactionPane);
        scrollPane.setFitToWidth(true);
        scrollPane.maxHeightProperty().bind(root.heightProperty());
        scrollPane.getStyleClass().add("background");

        root.getChildren().add(scrollPane);

        ReadOnlyListProperty<Transaction> txList = manager.getTransactionsProperty();
        txList.addListener((d, o, n) -> {
            VBox pane = (VBox) scrollPane.getContent();
            pane.getChildren().setAll(manager.getTiles());
        });

        stage.setScene(scene);

        scene.getStylesheets().add("accountable.css");

        stage.show();

        manager.add(List.of(
                new Transaction(
                        "salaire",
                        (byte) 0,
                        TransactionType.REVENUE,
                        ValueType.ABSOLUTE,
                        1377.9f
                ),
                new Transaction(
                        "repas",
                        (byte) 0,
                        TransactionType.BUDGET,
                        ValueType.ABSOLUTE,
                        300
                ),
                new Transaction(
                        "téléphone",
                        (byte) 0,
                        TransactionType.BILL,
                        ValueType.ABSOLUTE,
                        15
                ),
                new Transaction(
                        "assurance",
                        (byte) 0,
                        TransactionType.BILL,
                        ValueType.ABSOLUTE,
                        306.7f
                ),
                new Transaction(
                        "misc",
                        (byte) 1,
                        TransactionType.SAVINGS,
                        ValueType.ABSOLUTE,
                        192.8f
                ),
                new Transaction(
                        "train",
                        (byte) 2,
                        TransactionType.SAVINGS,
                        ValueType.ABSOLUTE,
                        166.5f
                ),
                new Transaction(
                        "épargne",
                        (byte) 5,
                        TransactionType.SAVINGS,
                        ValueType.REMAINDER,
                        0.25f
                ),
                new Transaction(
                        "chu",
                        (byte) 4,
                        TransactionType.SAVINGS,
                        ValueType.ALL,
                        0
                )
        ));
    }
}
