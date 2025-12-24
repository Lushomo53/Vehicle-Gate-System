package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.views.components.OTPInputField;

public class OTPVerificationView implements Screen {
    private Parent root;

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, "Verification");
    }

    @Override
    public Parent getRoot() { return root; }

    public Parent build() {
        Label title = new Label("Verification");
        title.getStyleClass().add("label-app-title");

        Label info = new Label("Enter the code sent to: username@domain.com");

        OTPInputField otpField = new OTPInputField(5);
        otpField.style("otp-field");
        otpField.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.getStyleClass().add("button-primary");

        otpField.filledProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) confirmBtn.fire();
        }));

        VBox card = new VBox(10, title, info, otpField, confirmBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMaxWidth(400);
        card.getStyleClass().add("login-card");

        VBox root =new VBox(10, card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new OTPVerificationView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
