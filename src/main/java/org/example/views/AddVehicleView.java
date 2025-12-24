package org.example.views;

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

public class AddVehicleView {
    public void show(Stage stage) {
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Add Vehicle");
    }

    private Parent build() {
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

        nameField.setOnAction(e -> modelField.requestFocus());
        modelField.setOnAction(e -> licenseField.requestFocus());
        licenseField.setOnAction(e -> ownerField.requestFocus());
        ownerField.setOnAction(e -> saveBtn.fire());

        VBox form = new VBox(8, namePrompt, nameField, modelPrompt, modelField, licensePrompt, licenseField, ownerPrompt, ownerField);
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

        VBox root = new VBox(14, card);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new AddVehicleView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
