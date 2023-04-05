package com.chomusuke.gui.stage;

import com.chomusuke.logic.Account;
import com.chomusuke.logic.Storage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Random;

public class AddAccountScreen {

    private static final int PADDING = 8;
    public static void show(Map<Byte, Account> balances) {
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

            popUp.close();
        });

        popUp.show();
    }
}
