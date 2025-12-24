package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class ResetPasswordView implements Screen {
    private Parent root;

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, "Reset Password");
    }

    @Override
    public Parent getRoot() { return root; }

    public Parent build() {
        Label title = new Label("Password Reset");
        title.getStyleClass().add("label-app-title");

        Label usernamePrompt = new Label("Username");
        usernamePrompt.setFont(new Font(12));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("input-field");

        Label currPasswordPrompt = new Label("Current Password");
        currPasswordPrompt.setFont(new Font(12));

        PasswordField currPasswordField = new PasswordField();
        currPasswordField.setPromptText("Enter Current Password");
        currPasswordField.getStyleClass().add("input-field");

        TextField currPasswordPlain = new TextField();
        currPasswordPlain.setPromptText("Enter Current Password");
        currPasswordPlain.getStyleClass().add("input-field");
        currPasswordPlain.textProperty().bindBidirectional(currPasswordField.textProperty());

        Label newPasswordPrompt = new Label("New Password");
        newPasswordPrompt.setFont(new Font(12));

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");
        newPasswordField.getStyleClass().add("input-field");

        TextField newPasswordPlain = new TextField();
        newPasswordPlain.setPromptText("Enter New Password");
        newPasswordPlain.getStyleClass().add("input-field");
        newPasswordPlain.textProperty().bindBidirectional(newPasswordField.textProperty());

        Label confirmPasswordPrompt = new Label("Confirm Password");
        confirmPasswordPrompt.setFont(new Font(12));

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");
        confirmPasswordField.getStyleClass().add("input-field");

        TextField confirmPasswordPlain = new TextField();
        confirmPasswordPlain.setPromptText("Confirm New Password");
        confirmPasswordPlain.getStyleClass().add("input-field");
        confirmPasswordPlain.textProperty().bindBidirectional(confirmPasswordField.textProperty());

        CheckBox showPassword = new CheckBox("Show");

        //implementation of show password action by binding states and visibility
        List.of(currPasswordPlain, newPasswordPlain, confirmPasswordPlain).forEach(textField -> {
            textField.managedProperty().bind(showPassword.selectedProperty());
            textField.visibleProperty().bind(showPassword.selectedProperty());
        });

        List.of(currPasswordField, newPasswordField, confirmPasswordField).forEach(passwordField -> {
            passwordField.managedProperty().bind(showPassword.selectedProperty().not());
            passwordField.visibleProperty().bind(showPassword.selectedProperty().not());
        });

        VBox form = new VBox(
                8,
                usernamePrompt,
                usernameField,
                currPasswordPrompt,
                currPasswordField,
                currPasswordPlain,
                newPasswordPrompt,
                newPasswordField,
                newPasswordPlain,
                confirmPasswordPrompt,
                confirmPasswordField,
                confirmPasswordPlain,
                showPassword
        );
        form.setAlignment(Pos.CENTER_LEFT);
        form.setFillWidth(false);
        form.setPadding(new Insets(10));
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).setMaxWidth(320);
                ((TextField) node).setPrefWidth(320);
            }
        });

        Button resetBtn = new Button("Reset");
        resetBtn.getStyleClass().add("button-primary");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        usernameField.setOnAction(e -> currPasswordField.requestFocus());
        currPasswordField.setOnAction(e -> newPasswordPlain.requestFocus());
        newPasswordPlain.setOnAction(e -> confirmPasswordField.requestFocus());
        confirmPasswordField.setOnAction(e -> resetBtn.fire());

        VBox cardContent = new VBox(10, title, form, resetBtn);
        cardContent.setPadding(new Insets(10));
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(16, cardContent);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox root = new VBox(16, card);
        root.setPadding(new Insets(16));
        root.setAlignment(Pos.CENTER);

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new ResetPasswordView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
