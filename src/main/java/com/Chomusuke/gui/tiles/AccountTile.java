package com.chomusuke.gui.tiles;

import static com.chomusuke.util.Dimensions.PAD;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AccountTile extends Tile {

    public AccountTile(String accountName, float balance) {
        super();

        Text accountNameText = new Text(accountName);
        Text balanceText = new Text(Float.toString(balance));

        VBox content = new VBox(accountNameText, balanceText);
        content.setSpacing(PAD);
        getChildren().add(content);
    }
}
