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

package com.chomusuke.gui.element.tmp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Locale;

public class Tile extends HBox {

    private final StringProperty valueString = new SimpleStringProperty();

    public Tile(String name, double value) {

        VBox textBox = new VBox();

        this.getChildren().addAll(textBox);

        Text nameText = new Text(name);
        Text valueText = new Text();
        valueText.textProperty().bind(valueString);
        setValueString(String.format(Locale.ROOT, "%.2f", value));

        textBox.getChildren().addAll(nameText, valueText);



        // ----- STYLE -----
        {
            textBox.getStyleClass().add("tileText");
            textBox.getChildren().forEach(text -> text.setStyle(
                    "-fx-font: 24 \"Arial Rounded MT Bold\"; " +
                            "-fx-fill: lightgray"
            ));
        }
    }

    public void setValueString(String text) {

        valueString.set(text);
    }



    protected static class ColorTag extends Rectangle {

        public static final int DEFAULT_HEIGHT = 68;
        private static final Color DEFAULT_COLOR = Color.web("#33CCFF");

        public ColorTag() {

            this(DEFAULT_HEIGHT);
        }

        public ColorTag(double height) {

            getStyleClass().add("colorTag");
            setHeight(height);
            setWidth(8);
            setFill(DEFAULT_COLOR);  // Blue
        }
    }
}
