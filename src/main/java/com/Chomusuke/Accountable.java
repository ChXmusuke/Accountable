package com.Chomusuke;
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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.Chomusuke.transactions.FundsManager;

public class Accountable extends Application {

    private static final int WIN_WIDTH = 360;
    private static final int WIN_HEIGHT = 640;
    private static final int PAD = 16;
    private static final int BUTTONS_HEIGHT = 0;
    private static final int INNER_WIDTH = WIN_WIDTH - 2 * PAD;
    private static final int INNER_HEIGHT = WIN_HEIGHT - 2 * PAD;
    private static final String CATEGORIES = "Categories";
    private static final String TAGS = "Tags";
    private static final String ADD_BUTTON = "+";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Storage
        FundsManager fm = new FundsManager();
        fm.createAccount("default", 0);

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
        VBox content = new VBox(newAccount);
        ScrollPane s1 = new ScrollPane(content);

        s1.setPrefSize(width, height);

        return s1;
    }

    private ScrollPane TxList(int width, int height) {
        Button addTx = new Button(ADD_BUTTON);

        VBox content = new VBox(addTx);
        ScrollPane s1 = new ScrollPane(content);
        s1.setPrefSize(width, height);

        return s1;
    }

    private void setStage(Stage stage, Pane mainPane) {
        stage.setScene(new Scene(mainPane, WIN_WIDTH, WIN_HEIGHT));

        stage.setTitle("Accountable");
        stage.setWidth(WIN_WIDTH);
        stage.setHeight(WIN_HEIGHT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("resources/icon.png")));

        stage.show();
    }
}
