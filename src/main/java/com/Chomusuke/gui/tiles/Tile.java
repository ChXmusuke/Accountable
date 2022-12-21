package com.chomusuke.gui.tiles;

import static com.chomusuke.util.Dimensions.*;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public abstract class Tile extends HBox {
     public Tile() {
         super();
         super.setWidth(WIN_WIDTH/2f);
         super.setPadding(new Insets(PAD, PAD, PAD, PAD));
     }
}
