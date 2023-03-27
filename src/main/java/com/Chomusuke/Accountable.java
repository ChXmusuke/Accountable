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

import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.SquareButton;
import com.chomusuke.gui.element.TransactionTile;
import com.chomusuke.gui.stage.AddFileScreen;
import com.chomusuke.gui.stage.AddTransactionScreen;
import com.chomusuke.logic.Storage;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.chomusuke.logic.Transaction;
import com.chomusuke.logic.TransactionList;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static javafx.collections.FXCollections.observableList;

public class Accountable extends Application {

    private static final int WINDOW_HEIGHT = 580;
    private static final double WINDOW_RATIO = 6/10.0;
    private static final int PADDING = 8;

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


        Calendar currentDate = GregorianCalendar.getInstance();

        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        root.getStyleClass().add("background");
        root.setPadding(new Insets(PADDING, PADDING, 0, PADDING));

        TransactionList manager = new TransactionList();

        VBox top = new VBox();
        top.setSpacing(PADDING);

        // Title of the app
        Text title = new Text("Accountable.");
        title.setId("title");
        top.setAlignment(Pos.CENTER);

        HBox controls = new HBox();

        // Selection boxes
        ChoiceBox<String> selectMonth = new ChoiceBox<>();

        ChoiceBox<String> selectYear = new ChoiceBox<>();
        selectYear.valueProperty().addListener(e -> {
            selectMonth.setItems(observableList(Storage.getAvailableMonths(selectYear.getValue())));
            if (selectMonth.getItems().size() == 0)
                selectMonth.getItems().add(Integer.toString(currentMonth));

            selectMonth.setValue(selectMonth.getItems().get(selectMonth.getItems().size()-1));
        });
        selectYear.setItems(observableList(Storage.getAvailableYears()));
        if (!selectYear.getItems().contains(Integer.toString(currentYear)))
            selectYear.getItems().add(Integer.toString(currentYear));
        selectYear.setValue(selectYear.getItems().get(selectYear.getItems().size()-1));

        // New file button
        SquareButton newFile = new SquareButton("new.png", a -> AddFileScreen.show(
                selectYear.itemsProperty().get()
        ));

        // Load button
        SquareButton load = new SquareButton("load.png", a -> {
            try {
                manager.setTransactionList(Storage.load(
                        Integer.parseInt(selectYear.getValue()),
                        Integer.parseInt(selectMonth.getValue())
                ));
            } catch (NumberFormatException ignored) {
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // Save button
        SquareButton save = new SquareButton("save.png", a -> {
            try {
                Storage.write(
                        manager.getTransactionsProperty().getValue(),
                        Integer.parseInt(selectYear.getValue()),
                        Integer.parseInt(selectMonth.getValue()));
            } catch (NumberFormatException ignored) {
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });

        controls.getChildren().addAll(newFile, load, selectYear, selectMonth, save);

        top.getChildren().addAll(title, controls);

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

        ReadOnlyListProperty<Transaction> txList = manager.getTransactionsProperty();
        txList.addListener((d, o, n) -> {
            VBox pane = (VBox) scrollPane.getContent();
            pane.getChildren().setAll(manager.getTiles());
            pane.getChildren().forEach(t -> t.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    AddTransactionScreen.show(manager, ((TransactionTile) t).getBaseTransaction());
                }
            }));
        });

        // "Add transaction" button
        PlusButton addTransaction = new PlusButton();

        addTransaction.setOnMouseClicked((e) -> AddTransactionScreen.show(manager));
        addTransaction.layoutXProperty().bind(content.widthProperty().subtract(PlusButton.RADIUS*2+PADDING));
        addTransaction.layoutYProperty().bind(content.heightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));

        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.SPACE) {
                AddTransactionScreen.show(manager);
            }
            event.consume();
        });

        content.getChildren().addAll(scrollPane, addTransaction);

        stage.setScene(scene);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setWidth(WINDOW_HEIGHT*WINDOW_RATIO);
        stage.setResizable(false);

        scene.getStylesheets().addAll(
                "stylesheets/accountable.css",
                "stylesheets/TransactionTile.css"
        );

        manager.setTransactionList(Storage.load(currentYear, currentMonth));

        stage.show();
    }
}
