package org.gatesystem.views;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.gatesystem.controller.VehicleController;
import org.gatesystem.controller.VehicleDeleteStatus;
import org.gatesystem.controller.VehicleFilterStatus;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.Vehicle;
import org.gatesystem.util.UIUtil;
import org.gatesystem.views.components.BackPane;
import org.gatesystem.views.components.FilterPane;
import org.gatesystem.views.components.MainMenuBar;
import org.gatesystem.views.components.VehicleTable;

import java.util.Optional;

public class ManageVehiclesView implements Screen {
    private Parent root;
    private final String title = "Manage Vehicles";

    @Override
    public Parent getRoot() { return root; }
    @Override
    public String getTitle() { return title; }

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, title);
    }

    private Parent build() {
        VehicleController controller = new VehicleController();

        MainMenuBar menuBar = new MainMenuBar();
        FilterPane filterPane = new FilterPane();
        VehicleTable vehicleTable = new VehicleTable();

        Button addBtn = new Button("Add");
        Button editBtn = new Button("Edit");
        Button deleteBtn = new Button("Delete");

        addBtn.getStyleClass().addAll("action-button", "admin-action");
        editBtn.getStyleClass().addAll("action-button", "admin-action");
        deleteBtn.getStyleClass().addAll("action-button", "admin-action");

        editBtn.disableProperty().bind(
                Bindings.size(
                        vehicleTable.getSelectionModel().getSelectedItems()
                ).isNotEqualTo(1)
        );

        deleteBtn.disableProperty().bind(
                vehicleTable.getSelectionModel().selectedItemProperty().isNull()
        );

        BackPane backPane = new BackPane();

        VBox centerContent = new VBox(8, backPane, filterPane, vehicleTable);

        HBox buttonPane = new HBox(8, addBtn, editBtn, deleteBtn);
        buttonPane.getStyleClass().add("action-pane");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        root.setTop(menuBar);
        root.setCenter(centerContent);
        root.setBottom(buttonPane);

        vehicleTable.updateTable(controller.getAllVehicles());

        addBtn.setOnAction(_-> {
            AddVehicleView addVehicleView = new AddVehicleView(controller, () -> {
                vehicleTable.updateTable(controller.updatedVehicles());
            });
            SessionManager.currentSession.switchScreen(addVehicleView);
            addVehicleView.show((Stage) root.getScene().getWindow());
        });

        deleteBtn.setOnAction(_ -> {
            Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                displayDialog(Alert.AlertType.ERROR, "Error", "No vehicle selected");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete Vehicle");
            confirm.setContentText(
                    "Are you sure you want to delete vehicle with plate:\n"
                            + selected.getLicensePlate()
            );

            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                VehicleDeleteStatus status = controller.deleteVehicle(selected);

                if (status == VehicleDeleteStatus.DELETE_SUCCESS) {
                    vehicleTable.updateTable(controller.updatedVehicles());
                    displayDialog(
                            Alert.AlertType.INFORMATION,
                            "Deleted",
                            "Vehicle deleted successfully"
                    );
                } else {
                    displayDialog(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to delete vehicle"
                    );
                }
            }
        });

        editBtn.setOnAction(_ -> {
            var selected = vehicleTable.getSelectionModel().getSelectedItem();

            EditVehicleView editView = new EditVehicleView(
                    controller,
                    selected,
                    () -> {
                        vehicleTable.updateTable(controller.updatedVehicles());
                    }
            );

            SessionManager.currentSession.switchScreen(editView);
            editView.show((Stage) root.getScene().getWindow());
        });


        filterPane.filter.setOnAction(_ -> {
            filterPane.searchField.getStyleClass().remove("input-error");
            filterPane.searchErrorLabel.setVisible(false);

            String search = filterPane.searchField.getText().trim();
            boolean filterBySelected = !filterPane.filterBy.getSelectionModel().isEmpty();
            String filterBy = filterPane.filterBy.getSelectionModel().getSelectedItem();

            VehicleFilterStatus status =
                    controller.filter(search, filterBySelected, filterBy);

            switch (status) {
                case FILTER_BY_NOT_SELECTED ->
                        displayDialog(Alert.AlertType.ERROR, "Error", "Please select filter by");

                case INVALID_LICENSE_PLATE ->
                        UIUtil.displayError(
                                "Invalid license plate format",
                                filterPane.searchField,
                                filterPane.searchErrorLabel,
                                true
                        );

                case FILTER_SUCCESS ->
                        vehicleTable.updateTable(controller.updatedVehicles());
            }
        });

        return root;
    }

    private void displayDialog(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            ManageVehiclesView manageVehiclesView = new ManageVehiclesView();
            SessionManager.currentSession.switchScreen(manageVehiclesView);
            manageVehiclesView.show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
