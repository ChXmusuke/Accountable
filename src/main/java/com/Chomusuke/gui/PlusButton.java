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

package com.chomusuke.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PlusButton extends StackPane {

    private static final String CROSS_COLOR = "-fx-fill: white";
    private static final String BUTTON_COLOR = "-fx-fill: #33CC33";
    public static final int RADIUS = 25;
    private static final int CROSS_ARM_LENGTH = 21;
    private static final double CROSS_ARM_RATIO = 1.0/7;

    public PlusButton() {
        Circle c = new Circle(RADIUS);
        Rectangle v = new Rectangle(CROSS_ARM_LENGTH, CROSS_ARM_LENGTH/7.0);
        Rectangle h = new Rectangle(CROSS_ARM_LENGTH*CROSS_ARM_RATIO, CROSS_ARM_LENGTH);

        c.setStyle(BUTTON_COLOR);

        v.setStyle(CROSS_COLOR);
        h.setStyle(CROSS_COLOR);

        getChildren().addAll(c, v, h);
    }
}
