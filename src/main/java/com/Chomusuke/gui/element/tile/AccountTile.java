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

package com.chomusuke.gui.element.tile;

import com.chomusuke.logic.Account;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Locale;

public class AccountTile extends Tile {

    private final Account baseAccount;

    public AccountTile(Account account) {
        super(account.getName(), account.getBalance());

        baseAccount = account;

        Pane tag = new Pane();
        ColorTag t1 = new ColorTag();
        ColorTag t2 = new ColorTag(ColorTag.DEFAULT_HEIGHT*account.getProgress());
        t2.setFill(Color.GREEN);
        tag.getChildren().addAll(t1, t2);

        getChildren().add(0, tag);

        if (account.getObjective() != 0) {
            setValueString(String.format(Locale.ROOT, "%.2f / %.2f", account.getBalance(), account.getObjective()));
        }
    }

    public Account getBaseAccount() {

        return baseAccount;
    }
}
