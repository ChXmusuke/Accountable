package com.chomusuke.gui.stage;

import com.chomusuke.transactions.Storage;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;

public class AddFileScreen {
    public static void show(List<String> availableYears) {
        Stage stage = new Stage();
        stage.setResizable(false);
        VBox root = new VBox();
        root.getStyleClass().add("background");
        stage.setScene(new Scene(root));

        HBox inputs = new HBox();

        TextField year = new TextField();
        TextField month = new TextField();
        inputs.getChildren().addAll(year, month);
        inputs.getChildren().forEach(n ->
                ((TextField) n).setTextFormatter(new TextFormatter<>(new IntegerStringConverter()))
        );
        month.textProperty().addListener(t -> {
            StringProperty monthText = (StringProperty) t;
            int v;
            try {
                v = Integer.parseInt(monthText.getValue());

                if (v < 0)
                    monthText.setValue(Integer.toString(1));
                else if (v > 12) {
                    monthText.setValue(Integer.toString(12));
                }
            } catch (NumberFormatException ignored) {
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Button submit = new Button("Ajouter");
        submit.setOnAction(a -> {
            int yearValue = Integer.parseInt(year.getText());
            int monthValue = Integer.parseInt(month.getText());
            if (year.getText().equals("")
                    || month.getText().equals("")
                    || monthValue < 1
                    || monthValue > 12) {
                return;
            }
            Storage.write(new ArrayList<>(), yearValue, monthValue);

            availableYears.add(year.getText());


            stage.close();
        });

        root.getChildren().addAll(inputs, submit);

        stage.show();
    }
}
