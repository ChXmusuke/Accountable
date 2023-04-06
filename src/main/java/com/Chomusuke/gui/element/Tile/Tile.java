/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.chomusuke.gui.element.tile;

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
