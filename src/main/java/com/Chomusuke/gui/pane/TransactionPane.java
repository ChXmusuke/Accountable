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

import java.util.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import com.chomusuke.Accountable.SceneID;
import com.chomusuke.gui.element.DateSelector;
import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.SquareButton;
import com.chomusuke.gui.element.tile.TransactionTile;
import com.chomusuke.gui.popup.AddFileScreen;
import com.chomusuke.gui.popup.AddTransactionScreen;
import com.chomusuke.logic.Account;
import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;

import static com.chomusuke.Accountable.PADDING;

public class TransactionPane extends ContentPane {

    private static final float REMAINDER_COLOR_THRESHOLD = 0.1f;

    private final VBox transactionPane;

    public TransactionPane(ObjectProperty<SceneID> selectedScene, TransactionList txList, Map<Byte, Account> balances, StringProperty year, StringProperty month) {

        // ----- TOP -----
        // Title of the app
        Text title = new Text("Accountable.");
        HBox titleContainer = new HBox(title);

        // Controls
        HBox controls = new HBox();

        SquareButton accounts = new SquareButton("wallet.png", a -> selectedScene.set(SceneID.ACCOUNTS));
        SquareButton newFile = new SquareButton("new.png", a -> new AddFileScreen().show());
        DateSelector dateSelector = new DateSelector();
        Text loadedDate = new Text();

        Text remainder = new Text();
        HBox remainderContainer = new HBox(remainder);



        // ----- SCROLLABLE -----
        transactionPane = new VBox();



        // ----- CONTENT -----
        // "Add transaction" button
        PlusButton addTransaction = new PlusButton();



        controls.getChildren().addAll(accounts, newFile, dateSelector, loadedDate);

        addToTop(titleContainer, controls, remainderContainer);
        setScrollableContent(transactionPane);
        addToContent(addTransaction);



        // ----- STYLE -----
        {
            title.setId("title");
            title.getStyleClass().add("stdText");
            titleContainer.setAlignment(Pos.CENTER);

            loadedDate.getStyleClass().add("stdText");
            HBox.setMargin(loadedDate, new Insets(0, 0, 0, PADDING));

            year.bindBidirectional(dateSelector.getYearProperty());
            month.bindBidirectional(dateSelector.getMonthProperty());

            remainder.setStyle("-fx-font: 18 'Arial Rounded MT Bold'");
            remainderContainer.setAlignment(Pos.BASELINE_LEFT);

            transactionPane.getStyleClass().add("background");
            transactionPane.setPadding(new Insets(0, 0, PADDING, 0));
            transactionPane.setSpacing(PADDING);
            transactionPane.prefWidthProperty().bind(getContentWidthProperty());
            transactionPane.prefHeightProperty().bind(getContentHeightProperty());

            addTransaction.layoutXProperty().bind(getContentWidthProperty().subtract(PlusButton.RADIUS*2+PADDING));
            addTransaction.layoutYProperty().bind(getContentHeightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));
        }



        // ----- EVENTS -----
        {
            // Transaction addition (Big Fat + Button)
            addTransaction.setOnMouseClicked((e) -> new AddTransactionScreen(txList, balances).show());

            // Colors for remainder
            remainder.textProperty().addListener((v, o, n) -> {
                if (Float.parseFloat(n) == 0)
                    remainder.setFill(Color.BLUE);
                else if (Float.parseFloat(n) < 0)
                    remainder.setFill(Color.RED);
                else if (Float.parseFloat(n) < txList.getTotalRevenue() * REMAINDER_COLOR_THRESHOLD)
                    remainder.setFill(Color.ORANGE);
                else
                    remainder.setFill(Color.GREEN);
            });

            // Update the currently selected date
            dateSelector.getMonthProperty().addListener((v, o, n) -> {
                if (n != null)
                    loadedDate.setText(String.format("%s/%s", year.get(), month.get()));
            });

            // Update the remainder
            txList.getTransactionList().addListener((ListChangeListener<? super Transaction>) e ->
                remainder.setText(String.format(Locale.ROOT, "%.2f", txList.getRemainder()))
            );
        }
    }

    public void update(TransactionList txs, Map<Byte, Account> balances) {

        // Tiles generation
        List<TransactionTile> tiles = new ArrayList<>();
        for (int i = 0; i < txs.getTransactionList().size(); i++) {
            Transaction tx = txs.getTransactionList().get(i);
            TransactionTile tile = new TransactionTile(tx, txs.getValues()[i]);

            // Event handler
            tile.setOnMouseClicked(m -> {
                if (m.getButton() == MouseButton.PRIMARY) {
                    new AddTransactionScreen(txs, tile.getBaseTransaction(), balances).show();
                }
            });

            tiles.add(tile);
        }

        transactionPane.getChildren().setAll(tiles);
    }
}
