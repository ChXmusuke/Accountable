package com.chomusuke.gui.stage;

import com.chomusuke.gui.element.Tile.AccountTile;
import com.chomusuke.logic.Account;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class AccountScreen {

    private static final int WINDOW_HEIGHT = 580;
    private static final double WINDOW_RATIO = 6/10.0;
    private static final int PADDING = 8;

    /**
     * Don't let anyone instantiate this class
     * TODO: create scene to replace in main window
     */
    private AccountScreen() {}

    public static void show(Map<Byte, Account> balances) {
        // ----- MEMORY -----
        ObservableMap<Byte, Account> observableBalances = FXCollections.observableMap(balances);



        // ----- DISPLAY -----
        Stage stage = new Stage();

        VBox root = new VBox();
        Scene scene = new Scene(root);

        Button add = new Button();

        VBox content = new VBox();
        ScrollPane scrollPane = new ScrollPane(content);

        root.getChildren().addAll(add, scrollPane);



        // ----- STYLE -----
        {
            stage.setScene(scene);
            stage.setHeight(WINDOW_HEIGHT);
            stage.setWidth(WINDOW_HEIGHT * WINDOW_RATIO);
            stage.setResizable(false);

            scene.getStylesheets().add("stylesheets/accountable.css");

            root.getStyleClass().add("background");
            root.setPadding(new Insets(PADDING));
            root.setSpacing(PADDING);

            add.setText("Créer un compte");

            content.getStyleClass().add("background");
            content.prefWidthProperty().bind(root.widthProperty());
            content.prefHeightProperty().bind(root.heightProperty());
            content.setSpacing(PADDING);

            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }



        // ----- EVENTS -----
        {
            add.setOnAction(e -> AddAccountScreen.show(observableBalances));

            observableBalances.addListener((MapChangeListener<? super Byte, ? super Account>) c ->
                updateDisplay(observableBalances, content)
            );
        }

        updateDisplay(observableBalances, content);

        stage.show();
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
