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
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

        // Vertical app layout
        VBox appLayout = new VBox(
                buttonsPane(INNER_WIDTH, BUTTONS_HEIGHT + 2 * PAD),
                lowerPane(INNER_WIDTH, INNER_HEIGHT - 64));

        BorderPane frame = new BorderPane();
        frame.setCenter(appLayout);
        BorderPane.setMargin(appLayout, new Insets(PAD, PAD, PAD, PAD));

        // Boilerplate code, init of window variables
        setStage(stage, frame);
    }

    private GridPane buttonsPane(int width, int height) {
        GridPane buttons = new GridPane();

        // Top buttons
        Button categories = new Button(CATEGORIES);
        Button tags = new Button(TAGS);
        buttons.getChildren().addAll(categories, tags);

        GridPane.setConstraints(categories, 0, 0);
        GridPane.setConstraints(tags, 1, 0);

        // Dimensions
        buttons.setPrefSize(width, height);

        return buttons;
    }

    private HBox lowerPane(int width, int height) {
        HBox p = new HBox(
                accountsList(width / 2, height),
                TxList(width / 2, height));

        p.setPrefSize(width, height);

        return p;
    }

    private ScrollPane accountsList(int width, int height) {
        Button newAccount = new Button(ADD_BUTTON);
        VBox content = new VBox(new AccountTilePane(fm), newAccount);
        ScrollPane s1 = new ScrollPane(content);

        s1.setPrefSize(width, height);

        newAccount.setOnMouseClicked((e) -> {
            Stage popUp = new Stage();
            VBox contents = new VBox();

            TextField accountName = new TextField();
            TextField initBalance = new TextField();
            Button createAccount = new Button(CREATE_ACCOUNT);
            initBalance.setTextFormatter(new TextFormatter<>(new FloatStringConverter()));
            contents.getChildren().addAll(accountName, initBalance, createAccount);

            contents.setPadding(new Insets(PAD, PAD, PAD, PAD));
            contents.setSpacing(PAD);
            Scene popUpScene = new Scene(contents);
            popUp.setScene(popUpScene);

            createAccount.setOnMouseClicked((c) -> {
                fm.createAccount(accountName.getText(), Float.parseFloat(initBalance.getText()));
                popUp.close();
            });

            popUp.show();
        });

        return s1;
    }

    // TODO: Transaction addition screen
    private ScrollPane TxList(int width, int height) {
        Button addTx = new Button(ADD_BUTTON);

        VBox content = new VBox(addTx);
        ScrollPane s1 = new ScrollPane(content);
        s1.setPrefSize(width, height);

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

        return s1;
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
