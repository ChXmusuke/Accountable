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
