package org.example.views;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.EntryLogController;
import org.example.controller.service.SessionManager;
import org.example.model.EntryLog;
import org.example.model.User.Role;
import javafx.scene.control.*;
import org.example.views.components.EntryLogTable;
import org.example.views.components.FilterPane;
import org.example.views.components.MainMenuBar;

import java.util.*;

public class DashboardView implements Screen {
    private final Role role;
    private HashMap<Role, List<Button>> buttons;
    private Parent root;

    public DashboardView() {
        role = SessionManager.currentSession.getCurrentUser().getRole();
    }

    @Override
    public Parent getRoot() { return root; }

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, "Dashboard");
    }

    private Parent build() {
        EntryLogController controller = new EntryLogController();

        FilterPane filterPane = new FilterPane();
        EntryLogTable table = new EntryLogTable();
        MainMenuBar menuBar = new MainMenuBar();

        buttons = new HashMap<>();
        Button manageVehiclesBtn = new Button("Manage Vehicles");
        Button viewAuditLogBtn = new Button("View Audit Logs");
        Button registerGuardBtn = new Button("Register Guard");
        Button checkInBtn = new Button("Check In");
        Button checkOutBtn = new Button("Check Out");

        buttons.put(Role.ADMIN, Arrays.asList(manageVehiclesBtn, viewAuditLogBtn, registerGuardBtn));
        buttons.put(Role.GUARD, Arrays.asList(checkInBtn, checkOutBtn));
        buttons.put(Role.GUEST, Collections.emptyList());

        BorderPane pane = new BorderPane();
        pane.getStyleClass().add("dashboard-root");
        pane.setTop(menuBar);
        pane.setCenter(new VBox(8, filterPane, table));
        HBox buttonPane = new HBox(8);
        configureRoles(buttonPane);

        buttonPane.getStyleClass().add("action-pane");
        buttons.getOrDefault(Role.ADMIN, Collections.emptyList()).forEach(btn -> btn.getStyleClass().addAll("action-button", "admin-action"));
        buttons.getOrDefault(Role.GUARD, Collections.emptyList()).forEach(btn -> btn.getStyleClass().addAll("action-button", "guest-action"));

        checkInBtn.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                    EntryLog log = table.getSelectionModel().getSelectedItem();

                    return log == null || log.getStatus() != EntryLog.Status.OUT;
                },
                        table.getSelectionModel().selectedItemProperty())
        );

        checkOutBtn.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            EntryLog log = table.getSelectionModel().getSelectedItem();

                            return log == null || log.getStatus() != EntryLog.Status.IN;
                        },
                        table.getSelectionModel().selectedItemProperty())
        );

        pane.setBottom(buttonPane);

        table.updateTable(controller.getAllLogs());
        return pane;
    }

    private void configureRoles(HBox buttonPane) {
        for (Map.Entry<Role, List<Button>> pair : buttons.entrySet()) {
            if (pair.getKey() == role) {
                pair.getValue().forEach(btn -> buttonPane.getChildren().add(btn));
            }
        }
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new DashboardView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
