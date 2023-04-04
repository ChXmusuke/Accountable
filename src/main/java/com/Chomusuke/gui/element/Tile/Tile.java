package com.chomusuke.gui.element.Tile;

import com.chomusuke.logic.Transaction;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Locale;

public class Tile extends HBox {

    protected final Rectangle colorTag;

    public Tile(String name, double value) {

        colorTag = new Rectangle();
        VBox textBox = new VBox();

        this.getChildren().addAll(colorTag, textBox);

        Text nameText = new Text(name);
        Text valueText = new Text(String.format(Locale.ROOT, "%.2f", value));

        textBox.getChildren().addAll(nameText, valueText);



        // ----- STYLE -----
        {
            colorTag.getStyleClass().add("colorTag");
            colorTag.setHeight(68);
            colorTag.setWidth(8);
            colorTag.setFill(Color.web("#33CCFF"));  // Blue

            textBox.getStyleClass().add("tileText");
            textBox.getChildren().forEach(text -> text.setStyle(
                    "-fx-font: 24 \"Arial Rounded MT Bold\"; " +
                            "-fx-fill: lightgray"
            ));
        }
    }
}
