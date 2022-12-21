package com.chomusuke.gui;

import com.chomusuke.gui.tiles.AccountTile;
import com.chomusuke.transactions.FundsManager;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class AccountTilePane extends VBox {

    public AccountTilePane(FundsManager fundsManager) {
        fundsManager.balancesProperty().addListener((obs, o, n) -> {
            getChildren().clear();
            for (String name : fundsManager.accountNamesProperty().keySet()) {
                if (!Objects.equals(name, "Out")) {
                    float balance = fundsManager.balancesProperty()
                            .get(fundsManager.accountNamesProperty()
                                    .get(name));
                    getChildren().add(new AccountTile(name, balance));
                }
            }
        });
    }
}
