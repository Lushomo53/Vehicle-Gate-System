package org.example.views;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controller.VehicleListController;
import org.example.views.components.FilterPane;
import org.example.views.components.MainMenuBar;
import org.example.views.components.VehicleTable;

public class ManageVehiclesView {
    public void show(Stage stage) {
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Manage Vehicles");
    }

    private Parent build() {
        VehicleListController controller = new VehicleListController();

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

        HBox buttonPane = new HBox(8, addBtn, editBtn, deleteBtn);
        buttonPane.getStyleClass().add("action-pane");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        root.setTop(menuBar);
        root.setCenter(new VBox(8, filterPane, vehicleTable));
        root.setBottom(buttonPane);

        vehicleTable.updateTable(controller.getAllVehicles());

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new ManageVehiclesView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
