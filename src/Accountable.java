/*  Accountable: a personal spending monitoring program
    Copyright (C) 2022  Artur Yukhanov

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

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Accountable extends Application {

    private static final int MIN_WIDTH = 360;
    private static final int MIN_HEIGHT = 640;
    private static final int PAD = 16;
    private static final String CATEGORIES = "Categories";
    private static final String TAGS = "Tags";
    private static final String ADD_TX = "Add Tx";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        // Top buttons
        HBox buttons = new HBox();

        Button categories = new Button(CATEGORIES);
        Button tags = new Button(TAGS);
        Button addTx = new Button(ADD_TX);
        buttons.getChildren().addAll(categories, tags, addTx);

        // Vertical app layout
        SplitPane appLayout = new SplitPane(buttons);
        appLayout.setOrientation(Orientation.HORIZONTAL);

        BorderPane frame = new BorderPane();
        frame.setCenter(appLayout);
        frame.setMargin(appLayout, new Insets(PAD, PAD, PAD, PAD));
        Scene scene = new Scene(frame, MIN_WIDTH, MIN_HEIGHT);

        stage.setTitle("Accountable");
        stage.setScene(scene);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);
        stage.setResizable(false);

        stage.show();
    }
}
