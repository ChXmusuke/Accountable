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

import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import com.chomusuke.Accountable.SceneID;
import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.tile.AccountTile;
import com.chomusuke.gui.popup.AddAccountScreen;
import com.chomusuke.logic.Account;
import com.chomusuke.logic.TransactionList;

/**
 * Provides a JavaFX pane containing account information.
 */
public class AccountPane extends BorderPane {

    private static final int PADDING = 8;

    private final TransactionList txList;
    private final VBox accountPane;

    /**
     * Constructor.
     *
     * @param selectedScene the scene currently selected
     * @param txList a transaction list
     * @param balances an account map
     */
    public AccountPane(ObjectProperty<SceneID> selectedScene, TransactionList txList, Map<Byte, Account> balances) {
        this.txList = txList;

        // ----- TOP -----
        VBox top = new VBox();

        // Controls
        HBox controls = new HBox();

        Button back = new Button("<-");



        // ----- CONTENT -----
        Pane content = new Pane();

        accountPane = new VBox();
        ScrollPane scrollPane = new ScrollPane(accountPane);

        PlusButton addAccount = new PlusButton();

        controls.getChildren().add(back);
        top.getChildren().add(controls);
        setTop(top);

        content.getChildren().addAll(scrollPane, addAccount);
        setCenter(content);



        // ----- STYLE -----
        {
            // General
            getStyleClass().add("background");
            setPadding(new Insets(PADDING, PADDING, 0, PADDING));

            // Top
            top.setSpacing(PADDING);
            top.setPadding(new Insets(0, 0, PADDING, 0));

            // Content
            content.setPadding(new Insets(PADDING));
            content.prefWidthProperty().bind(widthProperty());
            content.prefHeightProperty().bind(heightProperty());

            accountPane.getStyleClass().add("background");
            accountPane.setPadding(new Insets(0, 0, PADDING, 0));
            accountPane.setSpacing(PADDING);
            accountPane.prefWidthProperty().bind(content.widthProperty());
            accountPane.prefHeightProperty().bind(content.heightProperty());

            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            addAccount.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING));
            addAccount.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
        }



        // ----- EVENTS -----
        {
            back.setOnAction(e -> selectedScene.set(SceneID.TRANSACTIONS));
            addAccount.setOnMouseClicked(e -> new AddAccountScreen(balances, txList).show());
        }
    }

    /**
     * Updates the display.
     *
     * @param balances an account map
     */
    public void update(Map<Byte, Account> balances) {
        accountPane.getChildren().clear();

        for (Account a : balances.values()) {
            if (a.getBalance() >= 0) {
                AccountTile t = new AccountTile(a);

                t.setOnMouseClicked(m ->
                        new AddAccountScreen(balances, t.getBaseAccount(), txList).show()
                );

                accountPane.getChildren().add(t);
            }
        }
    }
}
