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
import com.chomusuke.gui.element.Tile.AccountTile;
import com.chomusuke.gui.stage.AddAccountScreen;
import com.chomusuke.logic.Account;
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

public class AccountPane extends VBox {

    private static final int PADDING = 8;

    public AccountPane(ObjectProperty<SceneID> selectedScene, Map<Byte, Account> balances) {

        // ----- MEMORY -----
        ObservableMap<Byte, Account> observableBalances = FXCollections.observableMap(balances);



        // ----- DISPLAY -----
        Button back = new Button("<-");
        Button add = new Button();

        VBox content = new VBox();
        ScrollPane scrollPane = new ScrollPane(content);

        getChildren().addAll(back, add, scrollPane);



        // ----- STYLE -----
        {
            getStyleClass().add("background");
            setPadding(new Insets(PADDING));
            setSpacing(PADDING);

            add.setText("Créer un compte");

            content.getStyleClass().add("background");
            content.prefWidthProperty().bind(widthProperty());
            content.prefHeightProperty().bind(heightProperty());
            content.setSpacing(PADDING);

            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }



        // ----- EVENTS -----
        {
            back.setOnAction(e -> selectedScene.set(SceneID.MAIN));
            add.setOnAction(e -> AddAccountScreen.show(observableBalances));

            observableBalances.addListener((MapChangeListener<? super Byte, ? super Account>) c ->
                    updateDisplay(observableBalances, content)
            );
        }

        updateDisplay(observableBalances, content);
    }

    private static void updateDisplay(Map<Byte, Account> balances, Pane p) {
        p.getChildren().clear();

        for (Account a : balances.values()) {
            AccountTile t = new AccountTile(a);

            t.setOnMouseClicked(m ->
                    AddAccountScreen.show(balances, t.getBaseAccount())
            );

            p.getChildren().add(t);
        }
    }
}
