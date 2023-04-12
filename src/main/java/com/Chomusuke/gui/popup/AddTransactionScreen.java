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

package com.chomusuke.gui.popup;

import com.chomusuke.logic.Account;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.chomusuke.logic.Transaction.*;

public class AddTransactionScreen extends PopUp {

    private static final int PADDING = 8;

    public AddTransactionScreen(TransactionList txList, Map<Byte, Account> accounts) {

        this(txList, null, accounts);
    }

    public AddTransactionScreen(TransactionList txList, Transaction t, Map<Byte, Account> accounts) {
        super(t != null);

        Map<String, Byte> names = new HashMap<>();
        for (byte b : accounts.keySet())
            names.put(accounts.get(b).getName(), b);

        GridPane content = new GridPane();

        TextField nameField = new TextField();
        ChoiceBox<TransactionType> tTypeField = new ChoiceBox<>();
        TextField valueField = new TextField();
        ChoiceBox<ValueType> vTypeField = new ChoiceBox<>();
        ChoiceBox<String> to = new ChoiceBox<>();

        content.add(nameField, 0, 0);
        content.add(valueField, 1, 0);
        content.add(tTypeField, 0, 1);
        content.add(vTypeField, 1, 1);

        content.add(to, 0, 2);

        setContent(content);



        // ----- STYLE -----
        {
            content.setHgap(PADDING);
            content.setVgap(PADDING);
            content.getStyleClass().add("background");

            tTypeField.setMaxWidth(Double.MAX_VALUE);

            vTypeField.setMaxWidth(Double.MAX_VALUE);

            to.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(to, Priority.ALWAYS);
        }



        // ----- EVENTS -----
        {
            valueField.setTextFormatter(new TextFormatter<>(c -> {
                if (c.isAdded()) {
                    try {
                        if (c.getText().length() == 1 && !c.getText().equals(".") && !c.getText().equals("-"))
                            Integer.parseInt(c.getText());
                        float v = 0;
                        if (!c.getControlNewText().equals("-"))
                            v = Float.parseFloat(c.getControlNewText());

                        ValueType sVType = vTypeField.getSelectionModel().getSelectedItem();
                        if (sVType != null && !sVType.equals(ValueType.ABSOLUTE)) {
                            if (v > 100) {
                                valueField.setText(Float.toString(100f));

                                throw new NumberFormatException();
                            } else if (v < 0) {
                                valueField.setText(Float.toString(0f));

                                throw new NumberFormatException();
                            }
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

                if (!Objects.equals(n, TransactionType.SAVINGS)) {
                    to.getSelectionModel().clearSelection();
                    to.setVisible(false);
                } else
                    to.setVisible(true);
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

            setSubmitAction(a -> {
                if (nameField.getText() == null || nameField.getText().equals("")
                        || valueField.getText() == null || valueField.getText().equals("")
                        || tTypeField.getValue() == null || vTypeField.getValue() == null
                        || to.getValue() == null && tTypeField.getValue() == TransactionType.SAVINGS)
                    return;

                try {
                    float v = Float.parseFloat(valueField.getText());
                    if (v < 0 && !tTypeField.getValue().equals(TransactionType.SAVINGS))
                        v = Math.abs(v);

                    Transaction newTransaction = new Transaction(
                            nameField.getText(),
                            tTypeField.getValue().equals(TransactionType.SAVINGS) ? names.get(to.getValue()) : (byte) 0,
                            tTypeField.getValue(),
                            vTypeField.getValue(),
                            v
                    );
                    txList.add(newTransaction, t);
                } catch (NumberFormatException exception) {

                    return;
                }

                close();
            });

            if (t != null) {
                setDeleteAction(a -> {
                    txList.remove(t);

                    close();
                });
            }
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
            } else {
                tTypeField.getSelectionModel().select(TransactionType.REVENUE);
            }

            to.setItems(FXCollections.observableArrayList(
                    names.keySet().stream()
                    .filter(e -> accounts.get(names.get(e)).getBalance() >= 0)
                    .collect(Collectors.toList())
            ));
        }
    }
}
