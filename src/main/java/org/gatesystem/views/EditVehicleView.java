package org.gatesystem.views;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.gatesystem.controller.VehicleController;
import org.gatesystem.model.Vehicle;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.views.components.BackPane;

public class EditVehicleView implements Screen {

    private Parent root;
    private final String title = "Edit Vehicle";

    private final VehicleController controller;
    private final Vehicle vehicle;
    private final Runnable onSuccess;

    public EditVehicleView(VehicleController controller, Vehicle vehicle, Runnable onSuccess) {
        this.controller = controller;
        this.vehicle = vehicle;
        this.onSuccess = onSuccess;
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
        Label appTitle = new Label("Edit Vehicle");
        appTitle.getStyleClass().add("label-app-title");

        Label namePrompt = new Label("Vehicle Name");
        namePrompt.setFont(new Font(12));

        TextField nameField = new TextField();
        nameField.getStyleClass().add("input-field");
        nameField.setText(vehicle.getName());

        Label modelPrompt = new Label("Vehicle Model");
        modelPrompt.setFont(new Font(12));

        TextField modelField = new TextField();
        modelField.getStyleClass().add("input-field");
        modelField.setText(vehicle.getModel());

        Label licensePrompt = new Label("License Plate");
        licensePrompt.setFont(new Font(12));

        TextField licenseField = new TextField();
        licenseField.getStyleClass().add("input-field");
        licenseField.setText(vehicle.getLicensePlate());
        licenseField.setDisable(true);

        Label ownerPrompt = new Label("Owner Name");
        ownerPrompt.setFont(new Font(12));

        TextField ownerField = new TextField();
        ownerField.getStyleClass().add("input-field");
        ownerField.setText(vehicle.getOwnerName());

        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("button-primary");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("button-secondary");

        HBox actionButtons = new HBox(8, saveBtn, cancelBtn);
        actionButtons.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        VBox form = new VBox(
                8,
                namePrompt, nameField,
                modelPrompt, modelField,
                licensePrompt, licenseField,
                ownerPrompt, ownerField
        );

        form.getChildren().forEach(node -> {
            if (node instanceof TextField tf) {
                tf.setMaxWidth(320);
                tf.setPrefWidth(320);
            }
        });

        form.setAlignment(Pos.CENTER_LEFT);
        form.setFillWidth(false);

        BackPane backPane = new BackPane();

        VBox cardContent = new VBox(10, appTitle, form, actionButtons, errorLabel);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(6, 2, 2, 2));

        VBox card = new VBox(cardContent);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox centerContent = new VBox(card);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(backPane);
        root.setCenter(centerContent);

        // Navigation only (no logic yet)
        cancelBtn.setOnAction(_ ->
                SessionManager.currentSession.switchToPreviousScreen()
        );

        saveBtn.setOnAction(_ -> {
            form.getChildren().forEach(node -> {
                if (node instanceof TextField tf) {
                    tf.getStyleClass().remove("input-error");
                }
            });
            errorLabel.setVisible(false);

            String name = nameField.getText().trim();
            String model = modelField.getText().trim();
            String owner = ownerField.getText().trim();

            var status = controller.updateVehicle(
                    vehicle.getLicensePlate(),
                    name,
                    model,
                    owner
            );

            switch (status) {
                case UPDATE_SUCCESS -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Vehicle updated successfully");
                    alert.showAndWait();

                    onSuccess.run();
                    SessionManager.currentSession.switchToPreviousScreen();
                }

                case EMPTY_FIELDS -> {
                    errorLabel.setText("All fields must be filled");
                    errorLabel.setVisible(true);

                    form.getChildren().forEach(node -> {
                        if (node instanceof TextField) {
                            node.getStyleClass().add("input-error");
                        }
                    });
                }

                case VEHICLE_NOT_FOUND ->
                        errorLabel.setText("Vehicle no longer exists");

                case INVALID_LICENSE_PLATE ->
                        errorLabel.setText("Invalid license plate format");
            }
        });

        return root;
    }

    /* =======================
       Test Harness
       ======================= */
    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            Vehicle mock = new Vehicle(
                    "Toyota",
                    "Corolla",
                    "ABC 1234",
                    "John Doe"
            );

            new EditVehicleView(
                    new VehicleController(),
                    mock,
                    () -> {}
            ).show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}

