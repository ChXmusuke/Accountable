package com.chomusuke.gui.element;

import com.chomusuke.logic.Storage;
import com.chomusuke.util.Time;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class DateSelector extends HBox {

    private final StringProperty year = new SimpleStringProperty();
    private final StringProperty month = new SimpleStringProperty();

    private static final int PADDING = 8;
    public DateSelector() {
        // Selection boxes
        ChoiceBox<String> yearSelector = new ChoiceBox<>();
        ChoiceBox<String> monthSelector = new ChoiceBox<>();

        year.bind(yearSelector.valueProperty());
        month.bind(monthSelector.valueProperty());

        yearSelector.setOnShown(e -> yearSelector.setItems(FXCollections.observableList(
                Storage.getAvailableYears()
        )));

        monthSelector.setOnShown(e -> monthSelector.setItems(FXCollections.observableList(
                Storage.getAvailableMonths(Integer.parseInt(year.getValue()))
        )));

        yearSelector.getSelectionModel().selectedItemProperty().addListener(e -> monthSelector.getSelectionModel()
                .clearSelection()
        );


        ObservableList<String> years = FXCollections.observableList(Storage.getAvailableYears());
        if (years.size() == 0) {
            Storage.write(new ArrayList<>(), Time.getCurrentYear(), Time.getCurrentMonth());
            years = FXCollections.observableList(Storage.getAvailableYears());
        }
        ObservableList<String> months = FXCollections.observableList(Storage.getAvailableMonths(Integer.parseInt(years.get(years.size()-1))));
        if (months.size() == 0)
            months.add(Integer.toString(Time.getCurrentMonth()));
        // Initial selection
        yearSelector.setItems(years);
        yearSelector.getSelectionModel().selectLast();
        monthSelector.setItems(months);
        monthSelector.getSelectionModel().selectLast();


        getChildren().addAll(yearSelector, monthSelector);
        setSpacing(PADDING);
    }

    public int getYearValue() {
        try {
            return Integer.parseInt(year.getValue());
        } catch (NumberFormatException ignored) {
            // Exception ignored
        }

        return -1;
    }

    public int getMonthValue() {
        try {
            return Integer.parseInt(month.getValue());
        } catch (NumberFormatException ignored) {
            // Exception ignored
        }

        return -1;
    }
}
