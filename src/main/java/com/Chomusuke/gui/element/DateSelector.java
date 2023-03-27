package com.chomusuke.gui.element;

import com.chomusuke.logic.Storage;
import com.chomusuke.util.Time;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;

public class DateSelector extends HBox {

    private final StringProperty year = new SimpleStringProperty();
    private final StringProperty month = new SimpleStringProperty();

    private static final int PADDING = 8;
    public DateSelector() {
        // Selection boxes
        ChoiceBox<String> selectYear = new ChoiceBox<>();
        ChoiceBox<String> selectMonth = new ChoiceBox<>();

        year.bind(selectYear.valueProperty());
        month.bind(selectMonth.valueProperty());

        selectYear.getSelectionModel().selectedItemProperty().addListener(e -> {
            if (selectYear.getSelectionModel().getSelectedItem() == null)
                return;

            try {
                int y = Integer.parseInt(selectYear.getSelectionModel()
                        .getSelectedItem());
                selectMonth.getItems().setAll(Storage.getAvailableMonths(y));

                if (selectMonth.getItems().size() == 0)
                    selectMonth.getItems().add(Integer.toString(Time.getCurrentMonth()));

                selectMonth.getSelectionModel().selectLast();
            } catch (NumberFormatException ignored) {
                // Exception ignored
            }
        });

        selectYear.setOnShown(a -> selectYear.getItems()
                .setAll(Storage.getAvailableYears()));

        selectMonth.setOnShown(a -> {
            try {
                int y = Integer.parseInt(selectYear.getSelectionModel()
                        .getSelectedItem());
                selectMonth.getItems()
                        .setAll(Storage.getAvailableMonths(y));
            } catch (NumberFormatException ignored) {
                // Exception ignored
            }
        });
        selectYear.getItems().setAll(Storage.getAvailableYears());
        if (!selectYear.getItems().contains(Integer.toString(Time.getCurrentYear())))
            selectYear.getItems().add(Integer.toString(Time.getCurrentYear()));
        selectYear.getSelectionModel().selectLast();

        getChildren().addAll(selectYear, selectMonth);

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
