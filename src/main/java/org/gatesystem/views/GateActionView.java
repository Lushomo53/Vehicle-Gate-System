package org.gatesystem.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.gatesystem.controller.CheckStatus;
import org.gatesystem.controller.EntryLogController;
import org.gatesystem.controller.GateAction;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.views.components.BackPane;

public class GateActionView implements Screen {
    private final GateAction action;
    private Parent root;
    private final String title;
    private final Runnable onSuccess;

    public GateActionView(GateAction action, Runnable onSuccess) {
        this.action = action;
        this.onSuccess = onSuccess;
        title = action == GateAction.CHECK_IN ? "Check In" : "Check Out";
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, title);
    }

    private Parent build() {
        EntryLogController controller = new EntryLogController();

        Label title = new Label(action == GateAction.CHECK_IN ? "Vehicle Check In" : "Vehicle Check Out");
        title.getStyleClass().add("label-app-title");

        Label prompt = new Label("License Plate");
        prompt.setFont(new Font(12));

        TextField plateField = new TextField();
        plateField.setPromptText("Enter License Plate");
        plateField.getStyleClass().add("input-field");

        Button actionBtn = new Button(action == GateAction.CHECK_IN ? "Check In" : "Check Out");
        actionBtn.getStyleClass().add("button-primary");

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("helper-error");

        VBox form = new VBox(10, prompt, plateField);
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).setPrefWidth(360);
                ((TextField) node).setMaxWidth(360);
            }
        });
        form.setPadding(new Insets(10));
        form.setFillWidth(false);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(10, title, form, actionBtn, errorLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setMaxWidth(400);
        card.getStyleClass().add("login-card");

        VBox centerContent = new VBox(16, card);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(new BackPane());
        root.setCenter(centerContent);

        plateField.setOnAction(_ -> actionBtn.fire());

        actionBtn.setOnAction(_ -> {
            plateField.getStyleClass().remove("input-error");
            errorLabel.setVisible(false);

            String plate = plateField.getText().trim().toUpperCase();
            CheckStatus status = action == GateAction.CHECK_IN
                    ? controller.checkIn(plate)
                    : controller.checkOut(plate);

            switch (status) {
                case INVALID_LICENSE_FORMAT -> {
                    plateField.getStyleClass().add("input-error");
                    plateField.requestFocus();
                    errorLabel.setText("Please enter a valid license plate");
                    errorLabel.setVisible(true);
                }

                case VEHICLE_NOT_FOUND -> alert("Vehicle Not Found",
                        "No vehicle exists with license plate " + plate);

                case ALREADY_CHECKED_IN -> alert("Already Checked In",
                        "This vehicle is already inside.");

                case ALREADY_CHECKED_OUT -> alert("Already Checked Out",
                        "This vehicle is not currently inside.");

                case CHECK_IN_SUCCESS, CHECK_OUT_SUCCESS -> {
                    alert("Success",
                            action == GateAction.CHECK_IN
                                    ? "Vehicle checked in successfully."
                                    : "Vehicle checked out successfully.");

                    onSuccess.run();
                    SessionManager.currentSession.switchToPreviousScreen();
                }
            }
        });

        return root;
    }

    private void alert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

