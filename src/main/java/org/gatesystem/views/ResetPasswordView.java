package org.gatesystem.views;

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
import org.gatesystem.controller.ResetPasswordStatus;
import org.gatesystem.controller.UserController;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.util.UIUtil;
import org.gatesystem.views.components.BackPane;

import java.util.List;

public class ResetPasswordView implements Screen {
    private Parent root;
    private final String title = "Reset Password";

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, title);
    }

    @Override
    public Parent getRoot() { return root; }
    @Override
    public String getTitle() { return title; }

    public Parent build() {
        Label title = new Label("Password Reset");
        title.getStyleClass().add("label-app-title");

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

        VBox cardContent = new VBox(10, title, form, resetBtn);
        cardContent.setPadding(new Insets(10));
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(16, cardContent);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox centerContent = new VBox(16, card);
        centerContent.setPadding(new Insets(16));
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(new BackPane());
        root.setCenter(centerContent);

        currPasswordField.setOnAction(_ -> switchFocus(showPassword.isSelected(), newPasswordField, newPasswordPlain));
        currPasswordPlain.setOnAction(_ -> switchFocus(showPassword.isSelected(), newPasswordField, newPasswordPlain));
        newPasswordField.setOnAction(_ -> switchFocus(showPassword.isSelected(), confirmPasswordField, confirmPasswordPlain));
        newPasswordPlain.setOnAction(_ -> switchFocus(showPassword.isSelected(), confirmPasswordField, confirmPasswordPlain));
        confirmPasswordField.setOnAction(_ -> resetBtn.fire());
        confirmPasswordPlain.setOnAction(_ -> resetBtn.fire());

        UserController controller = new UserController();

        resetBtn.setOnAction(_ -> {
            String currPassword = currPasswordField.isVisible() ? currPasswordField.getText().trim() : currPasswordPlain.getText().trim();
            String newPassword = newPasswordField.isVisible() ? newPasswordField.getText().trim() : newPasswordPlain.getText().trim();
            String confirmedPassword = confirmPasswordField.isVisible() ? confirmPasswordField.getText().trim() : confirmPasswordPlain.getText().trim();

            ResetPasswordStatus status = controller.resetPassword(currPassword, newPassword, confirmedPassword);

            switch (status) {
                case EMPTY_CURRENT_PASSWORD -> UIUtil.displayError(
                        "Please enter your current password",
                        currPasswordField.isVisible() ? currPasswordField : currPasswordPlain,
                        errorLabel,
                        true
                );

                case EMPTY_NEW_PASSWORD -> UIUtil.displayError(
                        "Please enter your new password",
                        newPasswordField.isVisible() ? newPasswordField : newPasswordPlain,
                        errorLabel,
                        true
                );

                case EMPTY_CONFIRM_PASSWORD -> UIUtil.displayError(
                        "Please confirm your new password",
                        confirmPasswordField.isVisible() ? confirmPasswordField : confirmPasswordPlain,
                        errorLabel,
                        true
                );

                case INCORRECT_PASSWORD -> UIUtil.displayError(
                        "Incorrect Password",
                        newPasswordField.isVisible() ? newPasswordField : newPasswordPlain,
                        errorLabel,
                        true
                );

                case NEW_PASSWORD_MISMATCH -> UIUtil.displayError(
                        "Confirmed password doesn't match",
                        confirmPasswordField.isVisible() ? confirmPasswordField : confirmPasswordPlain,
                        errorLabel,
                        true
                );

                case RESET_PASSWORD_SUCCESS -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Password reset successfully");
                    alert.showAndWait();

                    SessionManager.currentSession.switchToPreviousScreen();
                }
            }
        });

        return root;
    }

    private void switchFocus(
            boolean showPasswordSelected,
            TextField passwordField,
            TextField plainField
    ) {
        if (showPasswordSelected) {
            plainField.requestFocus();
        } else {
            passwordField.requestFocus();
        }
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
