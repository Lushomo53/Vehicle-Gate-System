package org.example.views.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class FilterPane extends VBox {
    public final TextField searchField = new TextField();
    public final ComboBox<String> filterBy = new ComboBox<>();
    public final DatePicker entryFrom = new DatePicker();
    public final DatePicker entryTo = new DatePicker();
    public final ComboBox<String> statusBox = new ComboBox<>();
    public final Button filter = new Button("Filter");

    public FilterPane() {
        init();
    }

    private void init() {
        searchField.setPromptText("Search");
        String[] filterByOptions = new String[] {
                "Car Name/Model",
                "License Plate",
                "Owner"
        };

        filterBy.setPromptText("Filter By:");
        filterBy.getItems().addAll(filterByOptions);

        entryFrom.setPromptText("Select Date From");
        entryTo.setPromptText("Select Date To");
        entryFrom.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
        entryTo.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        statusBox.getItems().addAll("ALL", "IN", "OUT");

        HBox form = new HBox(10, searchField, filterBy, entryFrom, entryTo, filter);
        this.getChildren().add(form);
        this.setPadding(new Insets(10));
        filter.getStyleClass().add("filter-button");
        form.getStyleClass().add("filter-pane");
    }
}
