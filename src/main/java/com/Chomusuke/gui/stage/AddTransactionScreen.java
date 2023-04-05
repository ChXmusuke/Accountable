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

import com.chomusuke.logic.Account;
import javafx.collections.FXCollections;
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

import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.chomusuke.logic.Transaction.*;

public class AddTransactionScreen {

    private static final int PADDING = 8;

    /**
     * Displays the transaction entry window.
     *
     * @param txList a {@code Transaction} list
     */
    public static void show(TransactionList txList, Map<Byte, Account> accounts) {

        show(txList, null, accounts);
    }

    /**
     * Displays the transaction entry window.
     * If t is set, pre-fills the fields for modification.
     *
     * @param txList a {@code Transaction} list
     * @param t a {@code Transaction}
     */
    public static void show(TransactionList txList, Transaction t, Map<Byte, Account> accounts) {
        Map<String, Byte> names = new HashMap<>();
        for (byte b : accounts.keySet())
            names.put(accounts.get(b).getName(), b);

        Stage stage = new Stage();
        GridPane p = new GridPane();
        Scene s = new Scene(p);

        TextField nameField = new TextField();
        ChoiceBox<TransactionType> tTypeField = new ChoiceBox<>();
        TextField valueField = new TextField();
        ChoiceBox<ValueType> vTypeField = new ChoiceBox<>();
        ChoiceBox<String> to = new ChoiceBox<>();

        HBox buttons = new HBox();
        Button submit = new Button(t != null ? "Modifier" : "Ajouter");

        buttons.getChildren().add(submit);

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



        // ----- STYLE -----
        {
            stage.setScene(s);
            stage.setResizable(false);

            p.setPadding(new Insets(PADDING));
            p.setHgap(PADDING);
            p.setVgap(PADDING);
            p.getStyleClass().add("background");

            p.add(nameField, 0, 0);
            p.add(valueField, 1, 0);
            p.add(tTypeField, 0, 1);
            p.add(vTypeField, 1, 1);

            p.add(to, 0, 2);
            p.add(buttons, 0, 3, 2, 1);

            s.getStylesheets().add("stylesheets/accountable.css");

            tTypeField.setMaxWidth(Double.MAX_VALUE);

            vTypeField.setMaxWidth(Double.MAX_VALUE);

            to.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(to, Priority.ALWAYS);

            buttons.setPadding(new Insets(PADDING * 2));
            buttons.setSpacing(PADDING);
            submit.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(submit, Priority.ALWAYS);
        }



        // ----- EVENTS -----
        {
            valueField.setTextFormatter(new TextFormatter<>(c -> {
                if (c.isAdded()) {
                    try {
                        if (c.getText().length() == 1 && !c.getText().equals("."))
                            Integer.parseInt(c.getText());
                        float v = Float.parseFloat(c.getControlNewText());

                        ValueType sVType = vTypeField.getSelectionModel().getSelectedItem();
                        if (sVType != ValueType.ABSOLUTE && sVType != null && v > 100) {
                            valueField.setText(Float.toString(100f));

                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException n) {
                        return null;
                    }
                }

                return c;
            }));

            tTypeField.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> {
                if (Objects.equals(n, TransactionType.REVENUE)) {
                    vTypeField.getSelectionModel().select(ValueType.ABSOLUTE);
                    vTypeField.setDisable(true);
                } else {
                    vTypeField.setDisable(false);
                }
            });

            vTypeField.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> {
                if (Objects.equals(n, ValueType.ALL)) {
                    valueField.setText(Float.toString(100f));
                    valueField.setDisable(true);
                } else {
                    valueField.setDisable(false);

                    if (!Objects.equals(n, ValueType.ABSOLUTE)) {

                        valueField.setText(valueField.getText());
                    }
                }
            });

            submit.setOnAction((a) -> {
                if (nameField.getText() == null || nameField.getText().equals("")
                        || valueField.getText() == null || valueField.getText().equals("")
                        || tTypeField.getValue() == null || vTypeField.getValue() == null ||
                        (to.getValue() == null && tTypeField.getValue() == TransactionType.SAVINGS))
                    return;

                Transaction newTransaction = new Transaction(
                        nameField.getText(),
                        tTypeField.getValue().equals(TransactionType.SAVINGS) ? names.get(to.getValue()) : (byte) 0,
                        tTypeField.getValue(),
                        vTypeField.getValue(),
                        Float.parseFloat(valueField.getText())
                );
                txList.add(newTransaction, t);

                stage.close();
            });
        }



        // ----- VALUE INIT -----
        {
            tTypeField.getItems().addAll(TransactionType.values());
            vTypeField.getItems().setAll(ValueType.values());

            if (t != null) {
                nameField.setText(t.name());
                valueField.setText(Float.toString(t.value()));
                tTypeField.getSelectionModel().select(t.transactionType());
                vTypeField.getSelectionModel().select(t.valueType());
                if (t.to() != 0)
                    to.getSelectionModel().select(accounts.get(t.to()).getName());
            }

            to.setItems(FXCollections.observableArrayList(names.keySet()));
        }

        stage.show();
    }
}

