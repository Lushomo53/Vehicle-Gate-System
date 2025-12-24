package org.example.views.components;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.beans.property.*;
import org.example.model.EntryLog;

import java.time.LocalDateTime;
import java.util.List;

public class EntryLogTable extends TableView<EntryLog> {
    public EntryLogTable() {
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<EntryLog, String> carNameCol = new TableColumn<>("Car Name");
        carNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCarName()));

        TableColumn<EntryLog, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));

        TableColumn<EntryLog, String> licenseCol = new TableColumn<>("License Plate");
        licenseCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLicensePlate()));

        TableColumn<EntryLog, String> ownerNameCol = new TableColumn<>("OwnerName");
        ownerNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOwnerName()));

        TableColumn<EntryLog, LocalDateTime> entryCol = new TableColumn<>("Entry Time");
        entryCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEntryTime()));

        TableColumn<EntryLog, LocalDateTime> exitCol = new TableColumn<>("Exit Time");
        exitCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getExitTime()));

        TableColumn<EntryLog, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().toString()));

        TableColumn<EntryLog, String> checkedByCol = new TableColumn<>("Checked IN/OUT By");
        checkedByCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().toString()));

        getColumns().addAll(carNameCol, modelCol, licenseCol, ownerNameCol, entryCol, exitCol, statusCol, checkedByCol);

        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void updateTable(List<EntryLog> logs) {
        setItems(FXCollections.observableArrayList(logs));
    }
}
