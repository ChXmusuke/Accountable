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

import com.chomusuke.gui.pane.AccountPane;
import com.chomusuke.gui.pane.TransactionPane;
import com.chomusuke.gui.popup.AddAccountScreen;
import com.chomusuke.gui.popup.AddTransactionScreen;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        Map<Byte, Account> balances = Storage.readAccounts();

        stage.setScene(new Scene(new Pane()));
        ObjectProperty<SceneID> selectedScene = new SimpleObjectProperty<>();

        selectedScene.addListener((s, o, n) -> {
            switch (n) {
                case MAIN ->
                        stage.getScene().setRoot(new TransactionPane(selectedScene, manager, balances));
                case ACCOUNTS ->
                        stage.getScene().setRoot(new AccountPane(selectedScene, balances));
            }
        });



        // Main
        {
            selectedScene.set(SceneID.MAIN);

            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_HEIGHT * WINDOW_RATIO);
            stage.setResizable(false);
            stage.setTitle("Accountable.");

            stage.getScene().getStylesheets().add("stylesheets/accountable.css");

            // Icon
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
        }

        // Transaction addition (space key)
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                switch (selectedScene.get()) {
                    case MAIN ->
                            new AddTransactionScreen(manager, balances).show();
                    case ACCOUNTS ->
                            new AddAccountScreen(balances).show();
                }
            }
            event.consume();
        });

        stage.show();
    }

    public enum SceneID {
        MAIN,
        ACCOUNTS
    }
}
