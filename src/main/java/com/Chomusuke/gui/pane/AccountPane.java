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
import com.chomusuke.logic.Storage;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class AccountPane extends Pane {

    private static final int PADDING = 8;

    public AccountPane(ObjectProperty<SceneID> selectedScene, Map<Byte, Account> balances) {

        // ----- MEMORY -----
        ObservableMap<Byte, Account> observableBalances = FXCollections.observableMap(balances);



        // ----- DISPLAY -----
        VBox content = new VBox();
        Button back = new Button("<-");
        PlusButton add = new PlusButton();

        VBox tilePane = new VBox();
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


            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            add.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
            add.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
        }



        // ----- EVENTS -----
        {
            back.setOnAction(e -> selectedScene.set(SceneID.TRANSACTIONS));
            add.setOnMouseClicked(e -> new AddAccountScreen(observableBalances).show());

            observableBalances.addListener((MapChangeListener<? super Byte, ? super Account>) c -> {
                Storage.writeAccounts(balances);
                updateDisplay(observableBalances, tilePane);
            });
        }

        updateDisplay(observableBalances, tilePane);
    }

    private static void updateDisplay(Map<Byte, Account> balances, Pane p) {
        p.getChildren().clear();

        for (Account a : balances.values()) {
            AccountTile t = new AccountTile(a);

            t.setOnMouseClicked(m ->
                    new AddAccountScreen(balances, t.getBaseAccount()).show()
            );

            p.getChildren().add(t);
        }
    }
}
