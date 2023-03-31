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

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

import com.chomusuke.logic.Storage;
import com.chomusuke.util.Time;

/**
 * Provides a node for selecting a date with {@code ChoiceBox}es.
 */
public class DateSelector extends HBox {

    private final ReadOnlyStringWrapper year = new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper month = new ReadOnlyStringWrapper();

    private static final int PADDING = 8;

    public DateSelector() {

        setSpacing(PADDING);

        // Selection boxes
        ChoiceBox<String> yearSelector = new ChoiceBox<>();
        ChoiceBox<String> monthSelector = new ChoiceBox<>();

        getChildren().addAll(yearSelector, monthSelector);

        year.bind(yearSelector.valueProperty());
        month.bind(monthSelector.valueProperty());

        List<String> years = Storage.getAvailableYears();
        List<String> months = Storage.getAvailableMonths(Integer.parseInt(years.get(years.size()-1)));



        // ----- EVENTS -----
        {
            // Loads available years on deployment of the ChoiceBox
            yearSelector.setOnShown(e -> yearSelector.setItems(FXCollections.observableList(
                    Storage.getAvailableYears()
            )));

            // Loads available months on deployment of the ChoiceBox
            monthSelector.setOnShown(e -> monthSelector.setItems(FXCollections.observableList(
                    Storage.getAvailableMonths(Integer.parseInt(year.getValue()))
            )));

            // Clears the selected month when another year is chosen
            yearSelector.getSelectionModel().selectedItemProperty().addListener(e -> monthSelector.getSelectionModel()
                    .clearSelection()
            );
        }

        if (years.size() == 0) {
            Storage.write(new ArrayList<>(), Time.getCurrentYear(), Time.getCurrentMonth());
            years = FXCollections.observableList(Storage.getAvailableYears());
        }
        if (months.size() == 0)
            months.add(Integer.toString(Time.getCurrentMonth()));



        // ----- INIT SELECT -----
        yearSelector.setItems(FXCollections.observableList(years));
        yearSelector.getSelectionModel().selectLast();
        monthSelector.setItems(FXCollections.observableList(months));
        monthSelector.getSelectionModel().selectLast();
    }

    /**
     * Returns the current value of the year {@code ChoiceBox}.
     *
     * @return the year, as an {@code int} value
     */
    public int getYearValue() {

        return Integer.parseInt(year.getValue());
    }

    /**
     * Returns the current value of the month {@code ChoiceBox}.
     *
     * @return the month, as an {@code int} value
     */
    public int getMonthValue() {

        return Integer.parseInt(month.getValue());
    }

    /**
     * Returns a read only property containing the month value.
     *
     * @return a {@code ReadOnlyStringProperty}
     */
    public ReadOnlyStringProperty getMonthProperty() {
        return month.getReadOnlyProperty();
    }
}
