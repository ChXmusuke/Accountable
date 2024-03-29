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

package com.chomusuke.gui.element;

import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;

/**
 * Provides a JavaFX Node that represents a square button
 * with an icon and an action.
 */
public class SquareButton extends Button {

    private static final int SIZE = 24;

    public SquareButton(String icon, EventHandler<ActionEvent> event) {

        // ----- ICON -----
        ImageView buttonIcon = new ImageView(icon);
        buttonIcon.setFitHeight(SIZE);
        buttonIcon.setPreserveRatio(true);

        setGraphic(buttonIcon);
        getStyleClass().add("background");



        // ----- ACTION -----
        setOnAction(event);
    }
}
