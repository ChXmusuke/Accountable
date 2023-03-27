package com.chomusuke.gui.element;

import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;

public class SquareButton extends Button {

    private static final int SIZE = 24;

    public SquareButton(String icon, EventHandler<ActionEvent> event) {
        ImageView buttonIcon = new ImageView(icon);
        buttonIcon.setFitHeight(SIZE);
        buttonIcon.setPreserveRatio(true);

        setGraphic(buttonIcon);
        getStyleClass().add("background");
        setOnAction(event);
    }
}
