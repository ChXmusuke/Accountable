package com.chomusuke.gui.tiles;

import static com.chomusuke.util.Dimensions.*;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public abstract class Tile extends HBox {
     public Tile() {
         super();
         setPadding(new Insets(PAD));
         setStyle("-fx-border-color: black;");
     }
}
