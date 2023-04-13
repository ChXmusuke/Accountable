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

package com.chomusuke;

import java.util.*;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import com.chomusuke.gui.pane.AccountPane;
import com.chomusuke.gui.pane.TransactionPane;
import com.chomusuke.gui.popup.AddAccountScreen;
import com.chomusuke.gui.popup.AddTransactionScreen;
import com.chomusuke.util.Time;
import com.chomusuke.logic.*;

/**
 * Main class of the Accountable application.
 * <p>
 * Manages the main UI elements.
 */
public class Accountable extends Application {

    private static final int WINDOW_HEIGHT = 580;
    private static final double WINDOW_RATIO = 6/10.0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        System.out.println("""
                ========================================================================
                        
                Accountable: a personal spending monitoring program\s
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

                Icons provided by <https://icons8.com>
                        
                ========================================================================
                """);


        // ----- MEMORY -----
        TransactionList manager = new TransactionList();
        ObservableMap<Byte, Account> balances = FXCollections.observableMap(Storage.readAccounts());
        StringProperty year = new SimpleStringProperty();
        StringProperty month = new SimpleStringProperty();



        // ----- WINDOW -----
        stage.setScene(new Scene(new Pane()));
        ObjectProperty<SceneID> selectedScene = new SimpleObjectProperty<>();
        TransactionPane transactions = new TransactionPane(selectedScene, manager, balances, year, month);
        AccountPane accounts = new AccountPane(selectedScene, manager, balances);

        selectedScene.addListener((s, o, n) -> {
            switch (n) {
                case TRANSACTIONS ->
                        stage.getScene().setRoot(transactions);
                case ACCOUNTS ->
                        stage.getScene().setRoot(accounts);
            }
        });



        // ----- STYLE -----
        {
            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_HEIGHT * WINDOW_RATIO);
            stage.setResizable(false);
            stage.setTitle("Accountable.");

            stage.getScene().getStylesheets().add("stylesheets/accountable.css");

            // Icon
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        }



        // ----- EVENTS -----
        {
            // Load the corresponding file in memory when selecting a year+month
            month.addListener((v, o, n) -> {
                if (n != null) {

                    int intYear = Integer.parseInt(year.get());
                    int intMonth = Integer.parseInt(month.get());

                    if (intYear >= 1 && intMonth >= 1)
                        manager.setTransactionList(Storage.read(intYear, intMonth));
                }
            });

            // Transaction list modification
            manager.getTransactionList().addListener((ListChangeListener<Transaction>) l -> {
                l.next();

                int intYear = Integer.parseInt(year.get());
                int intMonth = Integer.parseInt(month.get());

                if (!manager.setAllFlag()) {
                    List<Transaction> oldList = new ArrayList<>(l.getList());

                    if (l.wasRemoved()) {
                        Storage.write(
                                manager.getTransactionList(),
                                intYear,
                                intMonth
                        );

                        if (l.wasAdded())
                            oldList.set(l.getFrom(), l.getRemoved().get(0));
                        else
                            oldList.add(l.getFrom(), l.getRemoved().get(0));
                    } else {
                        Storage.write(
                                l.getAddedSubList().get(0),
                                intYear,
                                intMonth
                        );

                        oldList.remove(l.getFrom());
                    }

                    Account.ModMap.of(oldList)
                            .reverse()
                            .apply(balances);

                    Account.ModMap.of(new ArrayList<>(l.getList()))
                            .apply(balances);

                    Storage.writeAccounts(balances);
                }

                transactions.update(manager, balances);
                accounts.update(balances);
            });

            balances.addListener((MapChangeListener<? super Byte, ? super Account>) c -> {
                Storage.writeAccounts(balances);
                accounts.update(balances);
            });

            selectedScene.addListener(e -> {
                transactions.update(manager, balances);
                accounts.update(balances);
            });

            // Transaction addition (space key)
            stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode() == KeyCode.SPACE) {
                    switch (selectedScene.get()) {
                        case TRANSACTIONS -> new AddTransactionScreen(manager, balances).show();
                        case ACCOUNTS -> new AddAccountScreen(balances, manager).show();
                    }
                }
                event.consume();
            });
        }



        // ----- INIT -----
        {
            selectedScene.set(SceneID.TRANSACTIONS);
            accounts.update(balances);
            year.set(Integer.toString(Time.getCurrentYear()));
            month.set(Integer.toString(Time.getCurrentMonth()));
        }

        stage.show();
    }


    /**
     * This enum provides constants for determining the scene currently loaded.
     */
    public enum SceneID {
        TRANSACTIONS,
        ACCOUNTS
    }
}
