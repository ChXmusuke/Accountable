package com.chomusuke.gui.tiles;

import static com.chomusuke.util.Dimensions.PAD;

import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AccountTile extends Tile {

    private final SimpleFloatProperty balanceProperty = new SimpleFloatProperty();

    public AccountTile(String accountName, float balance) {
        super();

        Text accountNameText = new Text(accountName);
        Text balanceText = new Text(Float.toString(balance));
        balanceText.textProperty().bind(this.balanceProperty.asString());

        VBox content = new VBox(accountNameText, balanceText);
        content.setSpacing(PAD);
        super.getChildren().add(content);
    }

    public void updateBalance(float balance) {
        this.balanceProperty.setValue(balance);
    }
}
