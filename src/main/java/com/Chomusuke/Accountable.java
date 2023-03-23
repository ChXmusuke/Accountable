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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.chomusuke.transactions.Transaction;
import com.chomusuke.transactions.TransactionList;
import javafx.util.converter.FloatStringConverter;

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

        VBox top = new VBox();
        top.setPadding(new Insets(8));
        // Button to add a transaction
        Button addTransaction = new Button("+ transaction");
        addTransaction.setOnMouseClicked((e) -> {
            Stage tAdd = new Stage();
            tAdd.setResizable(false);

            GridPane p = new GridPane();
            p.setPadding(new Insets(8));
            p.setHgap(8);
            p.setVgap(8);

            TextField nameField = new TextField();
            ChoiceBox<TransactionType> tTypeField = new ChoiceBox<>();
            tTypeField.getItems().addAll(TransactionType.values());
            TextField valueField = new TextField();
            valueField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
            ChoiceBox<Transaction.ValueType> vTypeField = new ChoiceBox<>();
            vTypeField.getItems().setAll(ValueType.values());

            Button submit = new Button("-> Ajouter");
            submit.setOnMouseClicked((e2) -> {
                manager.add(new Transaction(
                        nameField.getText(),
                        (byte) 0,
                        tTypeField.getValue(),
                        vTypeField.getValue(),
                        Float.parseFloat(valueField.getText())
                ));

                tAdd.close();
            });

            p.add(nameField, 0, 0);
            p.add(tTypeField, 0, 1);
            p.add(valueField, 1, 0);
            p.add(vTypeField, 1, 1);

            p.add(submit, 0, 2, 1, 2);

            Scene s = new Scene(p);
            tAdd.setScene(s);
            tAdd.show();
        });

        top.getChildren().add(addTransaction);

        root.getChildren().addAll(top, scrollPane);

        ReadOnlyListProperty<Transaction> txList = manager.getTransactionsProperty();
        txList.addListener((d, o, n) -> {
            VBox pane = (VBox) scrollPane.getContent();
            pane.getChildren().setAll(manager.getTiles());
        });

        stage.setScene(scene);

        scene.getStylesheets().add("accountable.css");

        stage.show();
    }
}
