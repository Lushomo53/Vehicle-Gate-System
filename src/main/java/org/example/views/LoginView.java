package org.example.views;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.util.Objects;

public class LoginView {
    public static void show(Stage stage) {
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Login - Vehicle Gate System");
    }

    public static Parent build() {
        ImageView logoView = new ImageView();

        try {
            Image logo = new Image(Objects.requireNonNull(LoginView.class.getResourceAsStream("/images/gate_system_logo.jpeg")));
            logoView.setImage(logo);
            logoView.setFitWidth(120);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("Image not found");
        }

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("input-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("input-field");

        TextField passwordPlain = new TextField();
        passwordPlain.setVisible(false);
        passwordPlain.getStyleClass().add("input-field");

        CheckBox showPassword = new CheckBox("Show");

        passwordPlain.managedProperty().bind(showPassword.selectedProperty());
        passwordPlain.visibleProperty().bind(showPassword.selectedProperty());

        passwordField.managedProperty().bind(showPassword.selectedProperty().not());
        passwordField.visibleProperty().bind(showPassword.selectedProperty().not());

        passwordPlain.textProperty().bindBidirectional(passwordField.textProperty());

        Label helper = new Label("Enter your credentials");
        helper.getStyleClass().add("helper");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("button-primary");

        Hyperlink resetLink = new Hyperlink("Reset Password");
        resetLink.getStyleClass().add("reset-link");

        resetLink.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Reset flow would run here.", ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
        });

        VBox form = new VBox(8);
        form.setAlignment(Pos.CENTER_LEFT);
        form.getChildren().addAll(usernameField, passwordField, passwordPlain);

        form.setFillWidth(false);

        int fieldWidth = 320;

        usernameField.setMaxWidth(fieldWidth);
        passwordField.setMaxWidth(fieldWidth);
        passwordPlain.setMaxWidth(fieldWidth);

        usernameField.setPrefWidth(fieldWidth);
        passwordField.setPrefWidth(fieldWidth);
        passwordPlain.setPrefWidth(fieldWidth);

        HBox showRow = new HBox(8, showPassword, resetLink);
        showRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(
                8,
                form,
                showRow,
                errorLabel,
                loginBtn
        );

        cardContent.setPadding(new Insets(6, 2, 2, 2));
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(cardContent);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.setMaxWidth(fieldWidth + 40);

        VBox root = new VBox(14);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        if (logoView.getImage() != null) root.getChildren().add(logoView);
        root.getChildren().add(card);

        root.getStyleClass().add("v-spacing");

        loginBtn.setOnAction(e -> {
            usernameField.getStyleClass().remove("input-error");
            passwordField.getStyleClass().remove("input-error");
            passwordPlain.getStyleClass().remove("input-error");
            errorLabel.setVisible(false);

            String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
            String password = passwordField.isVisible() ? passwordField.getText() : passwordPlain.getText();

            if (username.isEmpty() || password.isEmpty()) {
                if (username.isEmpty()) usernameField.getStyleClass().add("input-error");

                if (password.isEmpty()) {
                    passwordField.getStyleClass().add("input-error");
                    passwordPlain.getStyleClass().add("input-error");
                }

                errorLabel.setText("Please don't leave any field empty");
                errorLabel.setVisible(true);

                if (username.isEmpty()) usernameField.requestFocus();
                else {
                    if (passwordField.isVisible()) passwordField.requestFocus();
                    else passwordPlain.requestFocus();
                }
                return;
            }

            boolean valid = username.equalsIgnoreCase("admin") && password.equals("admin");

            if (valid) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Login Successful (demo).", ButtonType.OK);
                a.setHeaderText(null);
                a.showAndWait();
            } else {
                passwordField.getStyleClass().add("input-error");
                usernameField.getStyleClass().add("input-error");
                passwordPlain.getStyleClass().add("input-error");
                errorLabel.setText("Invalid username or password");
                errorLabel.setVisible(true);
                passwordField.requestFocus();
            }
        });

        usernameField.setOnAction(e -> {
            if (passwordField.isVisible()) passwordField.requestFocus();
            else passwordPlain.requestFocus();
        });

        passwordField.setOnAction(e -> loginBtn.fire());
        passwordPlain.setOnAction(e -> loginBtn.fire());

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            LoginView.show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(LoginView.TestApp.class, args);
    }
}
