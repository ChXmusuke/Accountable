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

import java.util.ArrayList;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

import com.chomusuke.logic.Storage;

import static com.chomusuke.Accountable.PADDING;

/**
 * This class provides a JavaFX Stage made for transaction creation.
 */
public class AddFileScreen extends PopUp {

    /**
     * Don't let anyone instantiate this class
     */
    public AddFileScreen() {
        super();

        // ----- MAIN -----
        HBox content = new HBox();
        TextField year = new TextField();
        TextField month = new TextField();

        content.getChildren().addAll(year, month);
        content.setSpacing(PADDING);

        setContent(content);



        // ----- EVENTS -----
        year.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isAdded()) {
                try {
                    int n = Integer.parseInt(c.getControlNewText());

                    if (n < 0) {
                        year.setText(Integer.toString(~n+1));

                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException n) {
                    return null;
                }
            }

            return c;
        }));

        month.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isAdded()) {
                try {
                    int n = Integer.parseInt(c.getControlNewText());

                    if (1 > n || n > 12) {
                        month.setText(Integer.toString(Math.min(Math.max(n, 1), 12)));

                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException n) {
                    return null;
                }
            }

            return c;
        }));

        setSubmitAction(a -> {
            // The submission does not trigger if either one of the fields is empty
            if (year.getText().equals("")
                    || month.getText().equals("")) {
                return;
            }

            int yearValue = Integer.parseInt(year.getText());
            int monthValue = Integer.parseInt(month.getText());

            // Restrict inputs to realistic year and month values
            if (Integer.parseInt(year.getText()) < 1
                    || Integer.parseInt(month.getText()) > 12) {
                return;
            }

            // Writes an empty list, meaning creating an empty file at wanted location
            Storage.write(new ArrayList<>(), yearValue, monthValue);

            close();
        });
    }
}
