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

import com.chomusuke.gui.element.DateSelector;
import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.SquareButton;
import com.chomusuke.gui.element.TransactionTile;
import com.chomusuke.gui.stage.AddFileScreen;
import com.chomusuke.gui.stage.AddTransactionScreen;
import com.chomusuke.logic.Storage;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Accountable extends Application {

    private static final int WINDOW_HEIGHT = 580;
    private static final double WINDOW_RATIO = 6/10.0;
    private static final int PADDING = 8;
    private static final float REMAINDER_COLOR_THRESHOLD = 0.1f;

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

        BorderPane root = new BorderPane();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("stylesheets/accountable.css");

        stage.setScene(scene);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setWidth(WINDOW_HEIGHT*WINDOW_RATIO);
        stage.setResizable(false);

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));

        stage.show();

        root.getStyleClass().add("background");
        root.setPadding(new Insets(PADDING, PADDING, 0, PADDING));

        TransactionList manager = new TransactionList();

        VBox top = new VBox();
        top.setSpacing(PADDING);
        top.setPadding(new Insets(0, 0, PADDING, 0));

        // Title of the app
        Text title = new Text("Accountable.");
        title.setId("title");
        title.getStyleClass().add("stdText");
        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER);

        HBox controls = new HBox();

        DateSelector dateSelector = new DateSelector();

        Text loadedDate = new Text();
        loadedDate.getStyleClass().add("stdText");

        // New file button
        SquareButton newFile = new SquareButton("new.png", a -> AddFileScreen.show());

        dateSelector.getMonthProperty().addListener((v, o, n) -> {
            if (n != null) {
                int year = dateSelector.getYearValue();
                int month = Integer.parseInt(n);

                if (year >= 1 && month >= 1)
                    manager.setTransactionList(Storage.load(year, month));
            }
        });

        controls.getChildren().addAll(newFile, dateSelector, loadedDate);
        HBox.setMargin(loadedDate, new Insets(0, 0, 0, PADDING));

        Text remainder = new Text();
        remainder.setStyle("-fx-font: 18 'Arial Rounded MT Bold'");
        HBox remainderContainer = new HBox(remainder);
        remainderContainer.setAlignment(Pos.BASELINE_LEFT);

        remainder.textProperty().addListener((v, o, n) -> {
            if (Float.parseFloat(n) == 0)
                remainder.setFill(Color.BLUE);
            else if (Float.parseFloat(n) < 0)
                remainder.setFill(Color.RED);
            else if (Float.parseFloat(n) < manager.getTotalRevenue()*REMAINDER_COLOR_THRESHOLD) {
                remainder.setFill(Color.ORANGE);
            }
            else
                remainder.setFill(Color.GREEN);
        });

        top.getChildren().addAll(titleContainer, controls, remainderContainer);

        // Pane for content
        Pane content = new Pane();
        content.setPadding(new Insets(PADDING));

        root.setTop(top);
        root.setCenter(content);

        // Content
        VBox transactionPane = new VBox();
        transactionPane.getStyleClass().addAll("background");
        transactionPane.setSpacing(PADDING);
        transactionPane.setPadding(new Insets(0, 0, PADDING, 0));
        transactionPane.prefWidthProperty().bind(content.widthProperty());
        transactionPane.prefHeightProperty().bind(content.heightProperty());

        ScrollPane scrollPane = new ScrollPane(transactionPane);
        scrollPane.getStyleClass().add("scrollPane");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        manager.getTransactionList().addListener((ListChangeListener<Transaction>) l -> {
            l.next();

            if (!manager.setAllFlag()) {
                if (l.wasRemoved()) {
                    Storage.write(
                            manager.getTransactionList(),
                            dateSelector.getYearValue(),
                            dateSelector.getMonthValue()
                    );
                } else {
                    Storage.write(
                            l.getAddedSubList().get(0),
                            dateSelector.getYearValue(),
                            dateSelector.getMonthValue()
                    );
                }
            }

            // Display

            int year = dateSelector.getYearValue();
            int month = dateSelector.getMonthValue();
            loadedDate.setText(String.format("%s/%s", year, month));
            remainder.setText(String.format(Locale.ROOT, "%.2f", manager.getRemainder()));

            VBox pane = (VBox) scrollPane.getContent();

            List<Transaction> txList = manager.getTransactionList();
            // Tiles generation
            List<TransactionTile> tiles = new ArrayList<>();
            for (int i = 0 ; i < txList.size() ; i++) {
                TransactionTile tile = new TransactionTile(txList.get(i), manager.getValues()[i]);
                // Event handler
                tile.setOnMouseClicked(m -> {
                    if (m.getButton() == MouseButton.PRIMARY) {
                        AddTransactionScreen.show(manager, tile.getBaseTransaction());
                    }
                });

                tiles.add(tile);
            }

            pane.getChildren().setAll(tiles);
        });

        // Transaction addition
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                AddTransactionScreen.show(manager);
            }
            event.consume();
        });

        // "Add transaction" button
        PlusButton addTransaction = new PlusButton();

        addTransaction.setOnMouseClicked((e) -> AddTransactionScreen.show(manager));
        addTransaction.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING));
        addTransaction.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));

        content.getChildren().addAll(scrollPane, addTransaction);

        manager.setTransactionList(Storage.load(
                dateSelector.getYearValue(),
                dateSelector.getMonthValue()
        ));
        loadedDate.setText(String.format("%s/%s", dateSelector.getYearValue(), dateSelector.getMonthValue()));
    }
}
