package com.chomusuke.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class ButtonPlus extends StackPane {

    private static final String CROSS_COLOR = "-fx-fill: white";
    private static final String BUTTON_COLOR = "-fx-fill: #33CC33";

    public ButtonPlus() {
        Circle c = new Circle(25);
        Rectangle v = new Rectangle(21, 3);
        Rectangle h = new Rectangle(3, 21);

        c.setStyle(BUTTON_COLOR);

        v.setStyle(CROSS_COLOR);
        h.setStyle(CROSS_COLOR);

        getChildren().addAll(c, v, h);
    }
}
