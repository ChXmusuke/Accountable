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
import com.chomusuke.gui.TransactionTile;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.chomusuke.transactions.Transaction;
import com.chomusuke.transactions.TransactionList;
import javafx.util.converter.FloatStringConverter;

import static com.chomusuke.transactions.Transaction.TransactionType;
import static com.chomusuke.transactions.Transaction.ValueType;

public class Accountable extends Application {

    private static final int WINDOW_HEIGHT = 580;
    private static final double WINDOW_RATIO = 6/10.0;
    private static final int PADDING = 8;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        root.getStyleClass().add("background");
        root.setPadding(new Insets(PADDING, PADDING, 0, PADDING));

        TransactionList manager = new TransactionList();

        // Title of the app
        Text title = new Text("Accountable.");
        title.setId("title");
        BorderPane.setAlignment(title, Pos.CENTER);

        // Pane for content
        Pane content = new Pane();
        content.setPadding(new Insets(PADDING));

        root.setTop(title);
        root.setCenter(content);

        // Content
        VBox transactionPane = new VBox();
        transactionPane.getStyleClass().addAll("background");
        transactionPane.setSpacing(PADDING);
        transactionPane.setPadding(new Insets(0, 0, PADDING, 0));
        transactionPane.prefWidthProperty().bind(content.widthProperty());
        transactionPane.prefHeightProperty().bind(content.heightProperty());

        ScrollPane scrollPane = new ScrollPane(transactionPane);
        scrollPane.getStyleClass().add("scrollPane");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ReadOnlyListProperty<Transaction> txList = manager.getTransactionsProperty();
        txList.addListener((d, o, n) -> {
            VBox pane = (VBox) scrollPane.getContent();
            pane.getChildren().setAll(manager.getTiles());
            pane.getChildren().forEach(t -> t.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    addTransaction(manager, ((TransactionTile) t).getBaseTransaction());
                }
            }));
        });

        // "Add transaction" button
        PlusButton addTransaction = new PlusButton();

        addTransaction.setOnMouseClicked((e) -> addTransaction(manager));
        addTransaction.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING));
        addTransaction.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING));

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                addTransaction(manager);
            }
            event.consume();
        });

        content.getChildren().addAll(scrollPane, addTransaction);

        stage.setScene(scene);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setWidth(WINDOW_HEIGHT*WINDOW_RATIO);
        stage.setResizable(false);

        scene.getStylesheets().addAll("accountable.css", "TransactionTile.css");

        stage.show();
    }

    private void addTransaction(TransactionList txList) {
        addTransaction(txList, null);
    }

    private void addTransaction(TransactionList txList, Transaction t) {
        Stage tAdd = new Stage();
        tAdd.setResizable(false);

        GridPane p = new GridPane();
        p.setPadding(new Insets(PADDING));
        p.setHgap(PADDING);
        p.setVgap(PADDING);
        p.getStyleClass().add("background");

        // Construction of the GUI
        TextField nameField = new TextField();
        ChoiceBox<TransactionType> tTypeField = new ChoiceBox<>();
        tTypeField.getItems().addAll(TransactionType.values());
        tTypeField.setMaxWidth(Double.MAX_VALUE);
        TextField valueField = new TextField();
        valueField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
        ChoiceBox<Transaction.ValueType> vTypeField = new ChoiceBox<>();
        vTypeField.getItems().setAll(ValueType.values());
        vTypeField.setMaxWidth(Double.MAX_VALUE);

        if (t != null) {
            nameField.setText(t.name());
            tTypeField.setValue(t.transactionType());
            vTypeField.setValue(t.valueType());
            valueField.setText(Float.toString(t.value()));
        }

        HBox buttons = new HBox();
        buttons.setPadding(new Insets(PADDING*2));
        buttons.setSpacing(PADDING);

        // Submit button and its behavior
        Button submit = new Button(t != null ? "Modifier" : "Ajouter");
        submit.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(submit, Priority.ALWAYS);
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
            ),
                    t);

            tAdd.close();
        });

        buttons.getChildren().add(submit);

        // Delete button
        if (t != null) {
            Button delete = new Button("Supprimer");
            delete.setTextFill(Color.RED);
            delete.setOnAction(a -> {
                txList.remove(t);

                tAdd.close();
            });

            buttons.getChildren().add(delete);
        }

        p.add(nameField, 0, 0);
        p.add(valueField, 1, 0);
        p.add(tTypeField, 0, 1);
        p.add(vTypeField, 1, 1);

        p.add(buttons, 0, 2, 2, 1);

        Scene s = new Scene(p);
        s.getStylesheets().add("accountable.css");
        tAdd.setScene(s);
        tAdd.show();
    }
}
