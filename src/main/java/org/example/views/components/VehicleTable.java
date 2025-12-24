package org.example.views.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import org.example.model.Vehicle;

import java.util.List;


public class VehicleTable extends TableView<Vehicle> {
    public VehicleTable() {
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Vehicle, String> nameCol = new TableColumn<>("Car Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getModel()));

        TableColumn<Vehicle, String> licenseCol = new TableColumn<>("License Plate");
        licenseCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLicensePlate()));

        TableColumn<Vehicle, String> ownerCol = new TableColumn<>("Owner Name");
        ownerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOwnerName()));

        TableColumn<Vehicle, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus().toString()));

        getColumns().addAll(nameCol, modelCol, licenseCol, statusCol);

        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();

            row.setOnMouseEntered(e -> {
                Vehicle vehicle = row.getItem();

                if (vehicle != null) {
                    Tooltip tip = new Tooltip(
                            "Latest Entry Time: " + vehicle.getLatestEntryTime()
                            + "\nLatest Exit Time: " + vehicle.getLatestExitTime()
                    );
                    row.setTooltip(tip);
                }
            });

            return row;
        });
    }

    public void updateTable(List<Vehicle> updatedList) { setItems(FXCollections.observableArrayList(updatedList));}
}
