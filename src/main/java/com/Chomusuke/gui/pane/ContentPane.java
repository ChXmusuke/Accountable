package com.chomusuke.gui.pane;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.chomusuke.Accountable.PADDING;

/**
 * This class provides a specialized BorderPane node with a top part and
 * scrollable content.
 */
public abstract class ContentPane extends BorderPane {

    private final List<Node> top;
    private final ObjectProperty<Node> scrollable;
    private final List<Node> content;

    private final ReadOnlyDoubleProperty contentWidthProperty;
    private final ReadOnlyDoubleProperty contentHeightProperty;

    /**
     * Constructor.
     *
     * @param topContent the content to put on top of the pane
     * @param scrollableContent scrollableContent content. Added to a ScrollPane
     * @param additionalContent any node that may go above the rest
     */
    public ContentPane(Node topContent, Node scrollableContent, Node... additionalContent) {
        this();

        addToTop(topContent);
        setScrollableContent(scrollableContent);
        addToContent(additionalContent);
    }


    public ContentPane() {

        // ----- TOP -----
        VBox topNode = new VBox();



        // ----- CONTENT -----

        ScrollPane scrollPane = new ScrollPane();
        Pane contentPane = new Pane(scrollPane);

        super.setTop(topNode);
        super.setCenter(contentPane);

        contentWidthProperty = contentPane.widthProperty();
        contentHeightProperty = contentPane.heightProperty();

        // Modifiable
        top = topNode.getChildren();
        content = contentPane.getChildren();
        scrollable = scrollPane.contentProperty();



        // ----- STYLE -----
        {
            // General
            getStyleClass().add("background");
            setPadding(new Insets(PADDING, PADDING, 0, PADDING));

            // Top
            topNode.setSpacing(PADDING);
            topNode.setPadding(new Insets(0, 0, PADDING, 0));

            // Content
            contentPane.setPadding(new Insets(PADDING));
            contentPane.prefWidthProperty().bind(widthProperty());
            contentPane.prefHeightProperty().bind(heightProperty());

            scrollPane.getStyleClass().add("scrollPane");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
    }

    public void setScrollableContent(Node node) {
        scrollable.set(node);
    }

    public void addToTop(Node... nodes) {

        top.addAll(List.of(nodes));
    }

    public void addToContent(Node... nodes) {

        content.addAll(List.of(nodes));
    }

    public ReadOnlyDoubleProperty getContentWidthProperty() {

        return contentWidthProperty;
    }

    public ReadOnlyDoubleProperty getContentHeightProperty() {

        return contentHeightProperty;
    }
}
