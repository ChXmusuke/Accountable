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

import com.chomusuke.gui.PlusButton;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
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
        TransactionList manager = new TransactionList();

        BorderPane root = new BorderPane();
        root.getStyleClass().add("background");
        Scene scene = new Scene(root);

        VBox transactionPane = new VBox();
        transactionPane.getStyleClass().addAll("background", "transactions");
        transactionPane.prefWidthProperty().bind(root.widthProperty());
        transactionPane.prefHeightProperty().bind(root.heightProperty());

        ScrollPane scrollPane = new ScrollPane(transactionPane);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scrollPane");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        HBox controls = new HBox();
        controls.setPadding(new Insets(16));
        PlusButton addTransaction = new PlusButton();

        addTransaction.setOnMouseClicked((e) -> addTransaction(manager));
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) addTransaction(manager);
        });
        controls.setAlignment(Pos.CENTER_RIGHT);

        controls.getChildren().add(addTransaction);

        Text title = new Text("Accountable.");
        title.setId("title");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(8));

        root.setTop(title);
        root.setCenter(scrollPane);
        root.setBottom(controls);

        ReadOnlyListProperty<Transaction> txList = manager.getTransactionsProperty();
        txList.addListener((d, o, n) -> {
            VBox pane = (VBox) scrollPane.getContent();
            pane.getChildren().setAll(manager.getTiles());
        });

        stage.setScene(scene);
        stage.setWidth(333);
        stage.setHeight(580);
        stage.setResizable(false);

        scene.getStylesheets().addAll("accountable.css", "TransactionTile.css");

        stage.show();

        manager.add(
                new Transaction(
                        "salaire",
                        (byte) 0,
                        TransactionType.REVENUE,
                        ValueType.ABSOLUTE,
                        1377.9f
                )
        );
    }

    private void addTransaction(TransactionList txList) {
        Stage tAdd = new Stage();
        tAdd.setResizable(false);

        GridPane p = new GridPane();
        p.setPadding(new Insets(8));
        p.setHgap(8);
        p.setVgap(8);
        p.getStyleClass().add("background");

        TextField nameField = new TextField();
        ChoiceBox<TransactionType> tTypeField = new ChoiceBox<>();
        tTypeField.getItems().addAll(TransactionType.values());
        tTypeField.setMaxWidth(Double.MAX_VALUE);
        TextField valueField = new TextField();
        valueField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
        ChoiceBox<Transaction.ValueType> vTypeField = new ChoiceBox<>();
        vTypeField.getItems().setAll(ValueType.values());
        vTypeField.setMaxWidth(Double.MAX_VALUE);

        Button submit = new Button("-> Ajouter");
        submit.setMaxWidth(Double.MAX_VALUE);
        GridPane.setMargin(submit, new Insets(16));
        submit.setOnAction((e2) -> {
            if (nameField.getText() == null || nameField.getText().equals("")
                    || valueField.getText() == null || valueField.getText().equals("")
                    || tTypeField.getValue() == null || vTypeField.getValue() == null) {
                return;
            }
            txList.add(new Transaction(
                    nameField.getText(),
                    (byte) 0,
                    tTypeField.getValue(),
                    vTypeField.getValue(),
                    Float.parseFloat(valueField.getText())
            ));

            tAdd.close();
        });

        p.add(nameField, 0, 0);
        p.add(valueField, 1, 0);
        p.add(tTypeField, 0, 1);
        p.add(vTypeField, 1, 1);

        p.add(submit, 0, 2, 2, 1);
        GridPane.setHalignment(submit, HPos.CENTER);

        Scene s = new Scene(p);
        s.getStylesheets().add("accountable.css");
        tAdd.setScene(s);
        tAdd.show();
    }
}
