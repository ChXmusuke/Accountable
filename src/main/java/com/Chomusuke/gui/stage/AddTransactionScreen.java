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

package com.chomusuke.gui.stage;

import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;

public class AddTransactionScreen {

    private static final int PADDING = 8;

    public static void show(TransactionList txList) {
        show(txList, null);
    }

    public static void show(TransactionList txList, Transaction t) {
        Stage stage = new Stage();
        stage.setResizable(false);

        GridPane p = new GridPane();
        p.setPadding(new Insets(PADDING));
        p.setHgap(PADDING);
        p.setVgap(PADDING);
        p.getStyleClass().add("background");

        // Construction of the GUI
        TextField nameField = new TextField();
        ChoiceBox<Transaction.TransactionType> tTypeField = new ChoiceBox<>();
        tTypeField.getItems().addAll(Transaction.TransactionType.values());
        tTypeField.setMaxWidth(Double.MAX_VALUE);
        TextField valueField = new TextField();
        valueField.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
        ChoiceBox<Transaction.ValueType> vTypeField = new ChoiceBox<>();
        vTypeField.getItems().setAll(Transaction.ValueType.values());
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
        submit.setOnAction((a) -> {
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

            stage.close();
        });

        // Delete button
        if (t != null) {
            Button delete = new Button("Supprimer");
            delete.setTextFill(Color.RED);
            delete.setOnAction(a -> {
                txList.remove(t);

                stage.close();
            });

            buttons.getChildren().add(delete);
        }

        buttons.getChildren().add(submit);

        p.add(nameField, 0, 0);
        p.add(valueField, 1, 0);
        p.add(tTypeField, 0, 1);
        p.add(vTypeField, 1, 1);

        p.add(buttons, 0, 2, 2, 1);

        Scene s = new Scene(p);
        s.getStylesheets().add("stylesheets/accountable.css");

        stage.setScene(s);
        stage.show();
    }
}

