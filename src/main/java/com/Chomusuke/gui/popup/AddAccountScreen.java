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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

import java.util.Map;
import java.util.Random;

public class AddAccountScreen extends PopUp {

    private static final int PADDING = 8;

    public AddAccountScreen(Map<Byte, Account> balances) {

        this(balances, null);
    }

    public AddAccountScreen(Map<Byte, Account> balances, Account account) {
        super(account != null);

        byte id;
        if (account == null) {
            byte[] byteArray = new byte[1];
            Random r = new Random();
            while (byteArray[0] == 0 || balances.containsKey(byteArray[0]))
                r.nextBytes(byteArray);

            id = byteArray[0];
        } else {
            id = balances.keySet().stream()
                    .filter(f -> balances.get(f).getName().equals(account.getName()))
                    .toList().get(0);
        }

        HBox content = new HBox();
        TextField nameInput = new TextField();
        TextField objectiveInput = new TextField();
        content.getChildren().addAll(nameInput, objectiveInput);

        setContent(content);



        // ----- STYLE -----
        {
            content.setSpacing(PADDING);
        }



        // ----- EVENTS -----
        {
            objectiveInput.setTextFormatter(new TextFormatter<>(c -> {
                if (c.isAdded()) {
                    try {
                        if (c.getText().length() == 1 && !c.getText().equals("."))
                            Integer.parseInt(c.getText());
                    } catch (NumberFormatException n) {
                        return null;
                    }
                }

                return c;
            }));

            setSubmitAction(s -> {
                if (nameInput.getText().equals(""))
                    return;

                double value = account == null ? 0f : account.getBalance();
                balances.put(id, new Account(nameInput.getText(), value, Double.parseDouble(objectiveInput.getText())));

                close();
            });


            setDeleteAction(d -> {
                if (account != null && account.getBalance() == 0) {
                    balances.remove(id);

                    close();
                }
            });

            if (account != null) {
                nameInput.setText(account.getName());
                if (account.getObjective() >= 0)
                    objectiveInput.setText(Double.toString(account.getObjective()));
            }
        }
    }
}
