package com.chomusuke;
/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

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

import com.chomusuke.gui.AccountTilePane;
import com.chomusuke.transactions.FundsManager;
import static com.chomusuke.util.Dimensions.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.FloatStringConverter;

public class Accountable extends Application {

    private static final String CATEGORIES = "Categories";
    private static final String TAGS = "Tags";
    private static final String ADD_BUTTON = "+";
    private static final String CREATE_ACCOUNT = "Create account";

    private FundsManager fm;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Storage
        fm = new FundsManager();

        HBox buttonsPane = buttonsPane();
        HBox lowerPane = new HBox(accountList(), txList());

        // Fill remaining space with the lower pane
        VBox.setVgrow(lowerPane, Priority.ALWAYS);

        // Vertical app layout
        VBox appLayout = new VBox(
                buttonsPane,
                lowerPane);

        // Spacing and padding
        appLayout.setSpacing(PAD);
        appLayout.setPadding(new Insets(PAD));

        // Boilerplate code, init of window variables
        setStage(stage, appLayout);
    }

    private HBox buttonsPane() {
        HBox buttons = new HBox();
        buttons.setSpacing(PAD);

        // Top buttons
        Button categories = new Button(CATEGORIES);
        Button tags = new Button(TAGS);
        buttons.getChildren().addAll(categories, tags);

        return buttons;
    }

    private VBox accountList() {
        // Button to add an account
        Button newAccount = new Button(ADD_BUTTON);
        newAccount.setMaxWidth(Double.MAX_VALUE);
        // The button's functionnality
        newAccount.setOnMouseClicked((e) -> {
            Stage popUpWindow = new Stage();
            VBox contents = new VBox();
            Scene scene = new Scene(contents);

            popUpWindow.setScene(scene);
            popUpWindow.setResizable(false);

            contents.setPadding(new Insets(PAD));
            contents.setSpacing(PAD);

            // Account name field
            VBox accountNameField = new VBox();
            Label nameLabel = new Label("Nom du compte");
            TextField accountName = new TextField();
            nameLabel.setLabelFor(accountName);
            accountName.setMaxWidth(Double.MAX_VALUE);
            accountNameField.getChildren().addAll(nameLabel, accountName);

            // Initial balance field
            VBox initBalanceField = new VBox();
            Label balanceLabel = new Label("Solde initial");
            TextField initBalance = new TextField();
            initBalance.setMaxWidth(Double.MAX_VALUE);
            balanceLabel.setLabelFor(initBalance);
            initBalanceField.getChildren().addAll(balanceLabel, initBalance);

            // "Create account" button
            Button createAccount = new Button(CREATE_ACCOUNT);
            createAccount.setMaxWidth(Double.MAX_VALUE);
            initBalance.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));

            contents.getChildren().addAll(accountNameField, initBalanceField, createAccount);

            // Account creation on click
            createAccount.setOnMouseClicked((c) -> {
                float initValue;
                try {
                    initValue = Float.parseFloat(initBalance.getText());
                } catch (NumberFormatException n) {
                    initValue = 0;
                }
                fm.createAccount(accountName.getText(), initValue);

                popUpWindow.close();
            });

            popUpWindow.show();
        });

        // Scrollable list of account tiles
        ScrollPane accountList = new ScrollPane(new AccountTilePane(fm));
        accountList.setFitToWidth(true);
        accountList.setPrefWidth(accountList.getWidth());

        // Containing VBox
        VBox accountListPane = new VBox(newAccount, accountList);
        // Fill available space
        VBox.setVgrow(accountList, Priority.ALWAYS);
        HBox.setHgrow(accountListPane, Priority.ALWAYS);

        return accountListPane;
    }

    private VBox txList() {
        Button addTx = new Button(ADD_BUTTON);
        addTx.setMaxWidth(Double.MAX_VALUE);
        // The button's functionnality
        addTx.setOnMouseClicked((e) -> {
            Stage popUp = new Stage();
            VBox inputs = new VBox();
            inputs.setPadding(new Insets(PAD, PAD, PAD, PAD));
            inputs.setSpacing(PAD);

            TextField name = new TextField();
            DatePicker date = new DatePicker();
            TextField from = new TextField();
            TextField to = new TextField();
            TextField value = new TextField();

            HBox transactingAccounts = new HBox(from, to);
            transactingAccounts.setSpacing(PAD);

            inputs.getChildren().addAll(name, date, transactingAccounts, value);
            Scene popUpScene = new Scene(inputs);
            popUp.setScene(popUpScene);
            popUp.show();
        });

        // Scrollable list of transaction tiles
        // TODO: transaction list
        ScrollPane txList = new ScrollPane();
        txList.setFitToWidth(true);
        txList.setPrefWidth(txList.getWidth());

        // Containing VBox
        VBox txListPane = new VBox(addTx, txList);
        // Fill available space
        VBox.setVgrow(txList, Priority.ALWAYS);
        HBox.setHgrow(txListPane, Priority.ALWAYS);

        return txListPane;
    }

    private void setStage(Stage stage, Pane mainPane) {
        stage.setScene(new Scene(mainPane, WIN_WIDTH, WIN_HEIGHT));

        stage.setTitle("Accountable");
        stage.setWidth(WIN_WIDTH);
        stage.setHeight(WIN_HEIGHT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/icon.png")));

        stage.show();
    }
}
