package com.chomusuke.gui.stage;

import com.chomusuke.gui.element.Tile.Tile;
import com.chomusuke.logic.Account;
import com.chomusuke.logic.Storage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Random;

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



        // ----- DISPLAY -----
        Stage stage = new Stage();

        VBox root = new VBox();
        Scene scene = new Scene(root);

        Button add = new Button();

        FlowPane content = new FlowPane();
        ScrollPane scrollPane = new ScrollPane(content);
        for (byte a : balances.keySet()) {
            content.getChildren().add(new Tile(balances.get(a).getName(), balances.get(a).getBalance()));
        }

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
            content.setHgap(PADDING);
            content.setVgap(PADDING);

            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }



        // ----- EVENTS -----
        {
            add.setOnAction(e -> {
                byte[] b = new byte[1];
                Random r = new Random();
                while (b[0] == 0 || balances.containsKey(b[0])) {
                    r.nextBytes(b);
                }

                Stage popUp = new Stage();

                VBox popUpRoot = new VBox();
                Scene popUpScene = new Scene(popUpRoot);

                TextField nameInput = new TextField();
                Button submit = new Button("Créer");

                popUpRoot.getChildren().addAll(nameInput, submit);

                popUp.setScene(popUpScene);

                popUpRoot.setSpacing(PADDING);
                popUpRoot.setPadding(new Insets(PADDING));

                submit.setOnAction(s -> {

                    balances.put(b[0], new Account(nameInput.getText(), 0f));
                    Storage.writeAccounts(balances);

                    content.getChildren().add(new Tile(nameInput.getText(), 0f));

                    popUp.close();
                });

                popUp.show();
            });
        }

        stage.show();
    }
}