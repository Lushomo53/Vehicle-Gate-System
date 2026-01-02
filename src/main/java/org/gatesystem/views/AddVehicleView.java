package org.gatesystem.views;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.gatesystem.controller.VehicleController;
import org.gatesystem.controller.VehicleRegistrationStatus;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.util.UIUtil;
import org.gatesystem.views.components.BackPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class AddVehicleView implements Screen {
    private Parent root;
    private final String title = "Add Vehicle";
    private final VehicleController controller;
    private final Runnable onSuccess;

    public AddVehicleView(VehicleController controller, Runnable onSuccess) {
        this.controller = controller;
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
        Label appTitle = new Label("Register Vehicle");
        appTitle.getStyleClass().add("label-app-title");

        Label namePrompt = new Label("Vehicle Name");
        namePrompt.setFont(new Font(12));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Manufacturer Name, e.g. Toyota");
        nameField.getStyleClass().add("input-field");

        Label modelPrompt = new Label("Vehicle Model");
        modelPrompt.setFont(new Font(12));

        TextField modelField = new TextField();
        modelField.setPromptText("Enter Model");
        modelField.getStyleClass().add("input-field");

        Label licensePrompt = new Label("License Plate");
        licensePrompt.setFont(new Font(12));

        TextField licenseField = new TextField();
        licenseField.setPromptText("XXX-0000");
        licenseField.getStyleClass().add("input-field");

        Label ownerPrompt = new Label("Owner Name");
        ownerPrompt.setFont(new Font(12));

        TextField ownerField = new TextField();
        ownerField.setPromptText("Enter Owner Name");
        ownerField.getStyleClass().add("input-field");

        Button saveBtn = new Button("Save");
        saveBtn.getStyleClass().add("button-primary");
        saveBtn.setAlignment(Pos.CENTER);

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        VBox form = new VBox(8, appTitle, namePrompt, nameField, modelPrompt, modelField, licensePrompt, licenseField, ownerPrompt, ownerField);
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                int fieldWidth = 320;
                ((TextField) node).setMaxWidth(fieldWidth);
                ((TextField) node).setPrefWidth(fieldWidth);
            }
        });
        form.setAlignment(Pos.CENTER_LEFT);
        form.setFillWidth(false);

        VBox cardContent = new VBox(8, form, saveBtn, errorLabel);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(6, 2, 2, 2));

        VBox card = new VBox(8, cardContent);
        card.setPadding(new Insets(10));
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(360);

        BackPane backPane = new BackPane();

        VBox centerContent = new VBox(14, card);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(backPane);
        root.setCenter(centerContent);

        nameField.setOnAction(_ -> modelField.requestFocus());
        modelField.setOnAction(_ -> licenseField.requestFocus());
        licenseField.setOnAction(_ -> ownerField.requestFocus());
        ownerField.setOnAction(_ -> saveBtn.fire());

        saveBtn.setOnAction(_-> {
            form.getChildren().forEach(node -> {
                if (node instanceof TextField) {
                    node.getStyleClass().remove("input-error");
                }
            });
            errorLabel.setVisible(false);

            String name = nameField.getText().trim();
            String model = modelField.getText().trim();
            String licensePlate = licenseField.getText().trim();
            String ownerName = ownerField.getText().trim();

            VehicleRegistrationStatus status = controller.registerVehicle(name, model, licensePlate, ownerName);

            switch (status) {
                case REGISTRATION_SUCCESS -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Status");
                    alert.setContentText("Vehicle Successfully Registered");
                    alert.showAndWait();

                    onSuccess.run();
                    SessionManager.currentSession.switchToPreviousScreen();
                }

                case EMPTY_FIELDS -> {
                    AtomicBoolean isFirst = new AtomicBoolean(true);
                    form.getChildren().forEach(node -> {
                        if (node instanceof TextField) {
                            node.getStyleClass().add("input-error");
                            if (isFirst.get()) {
                                isFirst.set(false);
                                node.requestFocus();
                            }
                        }
                    });

                    errorLabel.setText("Please fill in all fields available");
                    errorLabel.setVisible(true);
                }

                case VEHICLE_ALREADY_EXISTS -> UIUtil.displayError("A Vehicle with the license plate \"" + licensePlate + "\" already exists", licenseField, errorLabel, true);
                case INVALID_LICENSE_PLATE -> UIUtil.displayError("Please enter a valid license plate in the valid format", licenseField, errorLabel, true);
            }
        });

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new AddVehicleView(new VehicleController(), () -> {}).show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
