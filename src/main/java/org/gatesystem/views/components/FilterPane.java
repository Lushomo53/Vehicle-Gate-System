package org.gatesystem.views.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class FilterPane extends VBox {
    public static final String CAR_NAME_OPTION = "Car Name/Model";
    public static final String LICENSE_PLATE_OPTION = "License Plate";
    public static final String OWNER_NAME_OPTION = "Owner Name";
    public static final String ALL_OPTION = "ALL";
    public static final String IN_OPTION = "IN";
    public static final String OUT_OPTION = "OUT";

    public final TextField searchField = new TextField();
    public final Label searchErrorLabel = new Label();
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

        searchErrorLabel.setVisible(false);
        searchErrorLabel.getStyleClass().add("input-error");

        VBox searchPane = new VBox(6, searchField, searchErrorLabel);

        filterBy.setPromptText("Filter By:");
        filterBy.getItems().addAll(CAR_NAME_OPTION, LICENSE_PLATE_OPTION, OWNER_NAME_OPTION);

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

        statusBox.getItems().addAll(ALL_OPTION, IN_OPTION, OUT_OPTION);
        statusBox.getSelectionModel().selectFirst();

        HBox form = new HBox(10, searchPane, filterBy, entryFrom, entryTo, filter);
        this.getChildren().add(form);
        this.setPadding(new Insets(10));
        filter.getStyleClass().add("filter-button");
        form.getStyleClass().add("filter-pane");
    }
}
