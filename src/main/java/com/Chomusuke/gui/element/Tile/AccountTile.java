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

package com.chomusuke.gui.element.Tile;

import com.chomusuke.logic.Account;

public class AccountTile extends Tile {

    private final Account baseAccount;

    public AccountTile(Account account) {
        super(account.getName(), account.getBalance());

        baseAccount = account;
    }

    public Account getBaseAccount() {

        return baseAccount;
    }
}
