package com.chomusuke.gui.popups;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    private final BooleanProperty showDeleteButton = new SimpleBooleanProperty(false);

    public PopUp() {

        this(new Pane());
    }

    public PopUp(Node content) {

        // ----- MAIN -----
        VBox root = new VBox();
        Scene scene = new Scene(root);

        Button submit = new Button("Confirmer");
        Button delete = new Button("Supprimer");

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

            delete.setTextFill(Color.RED);
        }

        showDeleteButton.addListener((b, o, n) ->
        {
            if (n && !o)
                buttons.getChildren().add(delete);
            else
                buttons.getChildren().remove(1);
        });
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

    protected void setShowDeleteButton(boolean show) {

        showDeleteButton.set(show);
    }
}
