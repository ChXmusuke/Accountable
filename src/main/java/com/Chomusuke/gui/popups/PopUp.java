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

package com.chomusuke.gui.popups;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class PopUp extends Stage {

    private static final int PADDING = 8;

    private final HBox buttons = new HBox();

    private final Button deleteButton;

    public PopUp() {

        this(new Pane());
    }

    public PopUp(Node content) {

        // ----- MAIN -----
        VBox root = new VBox();
        Scene scene = new Scene(root);

        Button submit = new Button("Confirmer");
        deleteButton = new Button("Supprimer");

        buttons.getChildren().add(submit);
        root.getChildren().addAll(content, buttons);

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

    protected void setContent(Node node) {

        ((VBox) getScene().getRoot()).getChildren()
                .set(0, node);
    }

    protected void setSubmitAction(EventHandler<ActionEvent> action) {

        ((Button) buttons.getChildren().get(0))
                .setOnAction(action);
    }

    protected void setDeleteAction(EventHandler<ActionEvent> action) {

        ((Button) buttons.getChildren().get(1))
                .setOnAction(action);
    }

    protected void showDeleteButton(boolean show) {

        if (show && buttons.getChildren().size() == 1)
            buttons.getChildren().add(deleteButton);
        else if (!show && buttons.getChildren().size() == 2)
            buttons.getChildren().remove(1);
    }
}
