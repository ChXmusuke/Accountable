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

package com.chomusuke.gui.popup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.chomusuke.Accountable.PADDING;

/**
 * This class provides a JavaFX stage used to create stuff.
 */
public abstract class PopUp extends Stage {

    private final HBox buttons = new HBox();

    /**
     * Default constructor.
     */
    public PopUp() {

        this(false);
    }

    /**
     * Constructor with parameter to show delete button.
     *
     * @param showDeleteButton a boolean
     */
    public PopUp(boolean showDeleteButton) {

        // ----- MAIN -----
        VBox root = new VBox();
        Scene scene = new Scene(root);

        Button submit = new Button("Confirmer");
        Button deleteButton = new Button("Supprimer");

        buttons.getChildren().add(submit);
        if (showDeleteButton)
            buttons.getChildren().add(deleteButton);
        root.getChildren().addAll(new Pane(), buttons);

        // ----- STYLE -----
        {
            setScene(scene);
            setResizable(false);

            scene.getStylesheets().add("stylesheets/accountable.css");

            root.getStyleClass().add("background");
            root.setPadding(new Insets(PADDING));
            root.setSpacing(PADDING);

            buttons.setSpacing(PADDING);
            buttons.setPadding(new Insets(PADDING));

            submit.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(submit, Priority.ALWAYS);

            deleteButton.setTextFill(Color.RED);
        }
    }

    /**
     * Sets the content of the stage.
     * Use to add buttons, text fields etc.
     *
     * @param node content
     */
    protected void setContent(Node node) {

        ((VBox) getScene().getRoot()).getChildren()
                .set(0, node);
    }

    /**
     * Defines the action to perform when the "submit" button is activated.
     *
     * @param action an event handler
     */
    protected void setSubmitAction(EventHandler<ActionEvent> action) {

        ((Button) buttons.getChildren().get(0))
                .setOnAction(action);
    }

    /**
     * Defines the action to perform when the "delete" button is activated.
     *
     * @param action an event handler
     */
    protected void setDeleteAction(EventHandler<ActionEvent> action) {

        if (buttons.getChildren().size() == 2) {
            ((Button) buttons.getChildren().get(1))
                    .setOnAction(action);
        }
    }
}
