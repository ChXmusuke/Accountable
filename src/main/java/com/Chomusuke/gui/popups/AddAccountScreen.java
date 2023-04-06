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

package com.chomusuke.gui.popups;

import com.chomusuke.logic.Account;
import com.chomusuke.logic.Storage;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.Random;

public class AddAccountScreen extends PopUp {

    public AddAccountScreen(Map<Byte, Account> balances) {

        this(balances, null);
    }

    public AddAccountScreen(Map<Byte, Account> balances, Account account) {
        super();

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

        TextField nameInput = new TextField();

        setSubmitAction(s -> {
            if (nameInput.getText().equals(""))
                return;

            double value = account == null ? 0f : account.getBalance();
            balances.put(id, new Account(nameInput.getText(), value));
            Storage.writeAccounts(balances);

            close();
        });

        if (account != null)
            nameInput.setText(account.getName());

        setContent(nameInput);
    }
}
