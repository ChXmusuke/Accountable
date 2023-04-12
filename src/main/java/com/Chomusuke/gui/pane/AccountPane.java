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

package com.chomusuke.gui.pane;

import com.chomusuke.Accountable.SceneID;
import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.tile.AccountTile;
import com.chomusuke.gui.popup.AddAccountScreen;
import com.chomusuke.logic.Account;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class AccountPane extends Pane {

    private static final int PADDING = 8;

    private final VBox tilePane;

    public AccountPane(ObjectProperty<SceneID> selectedScene, ObservableMap<Byte, Account> balances) {

        // ----- DISPLAY -----
        VBox content = new VBox();
        Button back = new Button("<-");
        PlusButton add = new PlusButton();

        tilePane = new VBox();
        ScrollPane scrollPane = new ScrollPane(tilePane);

        content.getChildren().addAll(back, scrollPane);
        getChildren().addAll(content, add);



        // ----- STYLE -----
        {
            content.getStyleClass().add("background");
            content.setPadding(new Insets(PADDING));
            content.setSpacing(PADDING);
            content.prefWidthProperty().bind(widthProperty());
            content.prefHeightProperty().bind(heightProperty());

            tilePane.setSpacing(PADDING);
            tilePane.getStyleClass().add("background");
            tilePane.prefWidthProperty().bind(content.widthProperty());
            tilePane.prefHeightProperty().bind(content.heightProperty());


            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            add.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
            add.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
        }



        // ----- EVENTS -----
        {
            back.setOnAction(e -> selectedScene.set(SceneID.TRANSACTIONS));
            add.setOnMouseClicked(e -> new AddAccountScreen(balances).show());
        }
    }

    public void update(Map<Byte, Account> balances) {
        tilePane.getChildren().clear();

        for (Account a : balances.values()) {
            if (a.getBalance() > 0) {
                AccountTile t = new AccountTile(a);

                t.setOnMouseClicked(m ->
                        new AddAccountScreen(balances, t.getBaseAccount()).show()
                );

                tilePane.getChildren().add(t);
            }
        }
    }
}
