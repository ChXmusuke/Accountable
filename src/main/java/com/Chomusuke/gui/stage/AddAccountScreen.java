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

        show(balances, null);
    }

    public static void show(Map<Byte, Account> balances, Account account) {
        byte id;
        if (account == null) {
            byte[] byteArray = new byte[1];
            Random r = new Random();
            while (byteArray[0] == 0 || balances.containsKey(byteArray[0]))
                r.nextBytes(byteArray);

            id = byteArray[0];
        } else {
            id = balances.keySet().stream()
                    .filter(f -> balances.get(f).getName().equals(account.getName()))
                    .toList().get(0);
        }


        Stage popUp = new Stage();

        VBox popUpRoot = new VBox();
        Scene popUpScene = new Scene(popUpRoot);

        TextField nameInput = new TextField();
        Button submit = new Button(account == null ? "Ajouter" : "Modifier");

        popUpRoot.getChildren().addAll(nameInput, submit);

        popUp.setScene(popUpScene);

        popUpRoot.setSpacing(PADDING);
        popUpRoot.setPadding(new Insets(PADDING));

        submit.setOnAction(s -> {
            if (nameInput.getText().equals(""))
                return;

            balances.put(id, new Account(nameInput.getText(), 0f));
            Storage.writeAccounts(balances);

            popUp.close();
        });

        if (account != null)
            nameInput.setText(account.getName());


        popUp.show();
    }
}
