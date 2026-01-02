package org.gatesystem.views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.gatesystem.controller.*;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.User;
import org.gatesystem.model.User.Role;
import javafx.scene.control.*;
import org.gatesystem.util.UIUtil;
import org.gatesystem.views.components.EntryLogTable;
import org.gatesystem.views.components.FilterPane;
import org.gatesystem.views.components.MainMenuBar;

import java.time.LocalDate;
import java.util.*;

public class DashboardView implements Screen {
    private final Role role;
    private HashMap<Role, List<Button>> buttons;
    private Parent root;
    private final String title = "Dashboard";

    public DashboardView() {
        role = SessionManager.currentSession.getCurrentUser().getRole();
    }

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

        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");
        root.setTop(menuBar);
        root.setCenter(new VBox(8, filterPane, table));
        HBox buttonPane = new HBox(8);
        configureRoles(buttonPane);

        buttonPane.getStyleClass().add("action-pane");
        buttons.getOrDefault(Role.ADMIN, Collections.emptyList()).forEach(btn -> btn.getStyleClass().addAll("action-button", "admin-action"));
        buttons.getOrDefault(Role.GUARD, Collections.emptyList()).forEach(btn -> btn.getStyleClass().addAll("action-button", "guest-action"));

        root.setBottom(buttonPane);

        table.updateTable(controller.getAllLogs());

        filterPane.filter.setOnAction(_ -> {
            filterPane.searchField.getStyleClass().remove("input-error");
            filterPane.searchErrorLabel.setVisible(false);
            filterPane.entryTo.getEditor().getStyleClass().remove("input-error");
            filterPane.entryFrom.getEditor().getStyleClass().remove("input-error");

            String search = filterPane.searchField.getText().trim();
            boolean filterBySelected = !filterPane.filterBy.getSelectionModel().isEmpty();
            String filterBy = filterPane.filterBy.getSelectionModel().getSelectedItem();
            String dateFromText = filterPane.entryFrom.getEditor().getText().trim();
            String dateToText = filterPane.entryTo.getEditor().getText().trim();
            LocalDate dateFrom = filterPane.entryFrom.getValue();
            LocalDate dateTo = filterPane.entryTo.getValue();
            String entryStatus = filterPane.statusBox.getSelectionModel().getSelectedItem();

            FilterStatus status = controller.filter(search, filterBySelected, filterBy, dateFromText, dateToText, dateFrom, dateTo, entryStatus);

            switch (status) {
                case FILTER_BY_NOT_SELECTED -> displayDialog(Alert.AlertType.ERROR, "Error", "Please select filter by for your search");
                case INVALID_LICENSE_PLATE -> UIUtil.displayError("Please enter a valid license plate" ,filterPane.searchField, filterPane.searchErrorLabel, true);
                case INVALID_DATE_FROM -> {
                    displayDialog(Alert.AlertType.ERROR, "Error", "Please enter a valid date format");
                    filterPane.entryFrom.getEditor().getStyleClass().add("input-error");
                    filterPane.entryFrom.getEditor().requestFocus();
                }
                case INVALID_DATE_TO -> {
                    displayDialog(Alert.AlertType.ERROR, "Error", "Please enter a valid date format");
                    filterPane.entryTo.getEditor().getStyleClass().add("input-error");
                    filterPane.entryTo.getEditor().requestFocus();
                }
                case FILTER_SUCCESS -> {
                    table.updateTable(controller.updatedLogs());
                }
            }
        });

        manageVehiclesBtn.setOnAction(_ -> {
            ManageVehiclesView manageVehiclesView = new ManageVehiclesView();
            SessionManager.currentSession.switchScreen(manageVehiclesView);
            manageVehiclesView.show((Stage) root.getScene().getWindow());
        });

        registerGuardBtn.setOnAction(_ -> {
            RegisterUserView registerUserView = new RegisterUserView();
            SessionManager.currentSession.switchScreen(registerUserView);
            registerUserView.show((Stage) root.getScene().getWindow());
        });

        viewAuditLogBtn.setDisable(true);

        checkInBtn.setOnAction(_ -> {
            GateActionView gateActionView = new GateActionView(GateAction.CHECK_IN, () -> table.updateTable(controller.updatedLogs()));
            SessionManager.currentSession.switchScreen(gateActionView);
            gateActionView.show((Stage) root.getScene().getWindow());
            table.updateTable(controller.updatedLogs());
        });

        checkOutBtn.setOnAction(_ -> {
            GateActionView gateActionView = new GateActionView(GateAction.CHECK_OUT, () -> table.updateTable(controller.updatedLogs()));
            SessionManager.currentSession.switchScreen(gateActionView);
            gateActionView.show((Stage) root.getScene().getWindow());
            table.updateTable(controller.updatedLogs());
        });

        menuBar.exit.setOnAction(_ -> {
            Platform.exit();
        });

        menuBar.logOutItem.setOnAction(_ -> {
            SessionManager.currentSession.clear();
            InitialScreen initialScreen = new InitialScreen();
            SessionManager.currentSession = new SessionManager(initialScreen);
            initialScreen.show((Stage) root.getScene().getWindow());
        });

        menuBar.resetPassword.setOnAction(_ -> {
            ResetPasswordView resetPasswordView = new ResetPasswordView();
            SessionManager.currentSession.switchScreen(resetPasswordView);
            resetPasswordView.show((Stage) root.getScene().getWindow());
        });

        menuBar.refreshItem.setOnAction(_ -> {
            table.updateTable(controller.updatedLogs());
        });

        menuBar.csvItem.setOnAction(_ -> {
            ExportStatus status = controller.exportEntryLogsToCSV(table.getItems(), root.getScene().getWindow());
            if (status == ExportStatus.EXPORT_SUCCESS) displayDialog(Alert.AlertType.INFORMATION, "Success", "Exported to CSV Successfully");
            else if (status == ExportStatus.EXPORT_FAILURE) displayDialog(Alert.AlertType.ERROR, "Failure", controller.getErrorStatus().getMessage());
        });

        return root;
    }

    private void displayDialog(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.showAndWait();
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
            SessionManager.currentSession.switchUser(new User("ADMIN", "admin@gmail.com", Role.GUARD, "Lushomo", "Lungo", "486517/16/1"));
            new DashboardView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
