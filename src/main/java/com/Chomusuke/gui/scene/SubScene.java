package com.chomusuke.gui.scene;

import com.chomusuke.Accountable.SceneID;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public abstract class SubScene extends Scene {

    protected ObjectProperty<SceneID> selectedScene;

    public SubScene(ObjectProperty<SceneID> selectedScene) {
        super(new Pane());
        this.selectedScene = selectedScene;
    }
}
