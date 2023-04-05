package com.chomusuke.gui.scene;

import com.chomusuke.Accountable.SceneID;
import com.chomusuke.gui.element.Tile.AccountTile;
import com.chomusuke.gui.stage.AddAccountScreen;
import com.chomusuke.logic.Account;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Map;

public class AccountScene extends SubScene {

    private static final int PADDING = 8;

    public AccountScene(ObjectProperty<SceneID> selectedScene, Map<Byte, Account> balances) {
        super(selectedScene);
        VBox root = new VBox();
        setRoot(root);

        // ----- MEMORY -----
        ObservableMap<Byte, Account> observableBalances = FXCollections.observableMap(balances);



        // ----- DISPLAY -----
        Button back = new Button("<-");
        Button add = new Button();

        VBox content = new VBox();
        ScrollPane scrollPane = new ScrollPane(content);

        root.getChildren().addAll(back, add, scrollPane);



        // ----- STYLE -----
        {
            getStylesheets().add("stylesheets/accountable.css");

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
            back.setOnAction(e -> selectedScene.set(SceneID.MAIN));
            add.setOnAction(e -> AddAccountScreen.show(observableBalances));

            observableBalances.addListener((MapChangeListener<? super Byte, ? super Account>) c ->
                    updateDisplay(observableBalances, content)
            );
        }

        updateDisplay(observableBalances, content);
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
