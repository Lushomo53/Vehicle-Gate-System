package org.gatesystem.views;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import org.gatesystem.controller.LoginStatus;
import org.gatesystem.controller.UserController;
import org.gatesystem.controller.service.SessionManager;

import java.util.Objects;

public class LoginView implements Screen {
    private Parent root;
    private final String title = "Login";

    @Override
    public Parent getRoot() { return root; }
    @Override
    public String getTitle() { return title; }

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, title);
    }

    public Parent build() {
        UserController controller = new UserController();

        Label title = new Label("Login");
        title.setAlignment(Pos.CENTER);
        title.getStyleClass().add("label-app-title");

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

        Label usernamePrompt = new Label("Username");
        usernamePrompt.setFont(new Font(12));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Your Username");
        usernameField.getStyleClass().add("input-field");

        Label passwordPrompt = new Label("Password");
        passwordPrompt.setFont(new Font(12));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.getStyleClass().add("input-field");

        TextField passwordPlain = new TextField();
        passwordPlain.setPromptText("Enter Password");
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

        Hyperlink recoveryLink = new Hyperlink("Forgot your password?");
        recoveryLink.getStyleClass().add("reset-link");

        VBox form = new VBox(8);
        form.setAlignment(Pos.CENTER_LEFT);
        form.getChildren().addAll(usernamePrompt, usernameField, passwordPrompt, passwordField, passwordPlain);

        form.setFillWidth(false);

        int fieldWidth = 320;

        usernameField.setMaxWidth(fieldWidth);
        passwordField.setMaxWidth(fieldWidth);
        passwordPlain.setMaxWidth(fieldWidth);

        usernameField.setPrefWidth(fieldWidth);
        passwordField.setPrefWidth(fieldWidth);
        passwordPlain.setPrefWidth(fieldWidth);

        HBox showRow = new HBox(8, showPassword, recoveryLink);
        showRow.setAlignment(Pos.CENTER_LEFT);

        VBox cardContent = new VBox(
                8,
                title,
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

        recoveryLink.setOnAction(_-> {
            PasswordRecoveryView recoveryView = new PasswordRecoveryView();
            SessionManager.currentSession.switchScreen(recoveryView);
            recoveryView.show((Stage) root.getScene().getWindow());
        });

        loginBtn.setOnAction(e -> {
            usernameField.getStyleClass().remove("input-error");
            passwordField.getStyleClass().remove("input-error");
            passwordPlain.getStyleClass().remove("input-error");
            errorLabel.setVisible(false);

            String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
            String password = passwordField.isVisible() ? passwordField.getText() : passwordPlain.getText();

            LoginStatus status = controller.authenticateUser(username, password);

            switch (status) {
                case LOGIN_SUCCESSFUL -> {
                    DashboardView dashboardView = new DashboardView();
                    SessionManager.currentSession.switchScreen(dashboardView);
                    dashboardView.show((Stage) root.getScene().getWindow());
                }
                case EMPTY_USERNAME -> {
                    errorLabel.setText("Please enter your username!");
                    errorLabel.setVisible(true);
                    usernameField.getStyleClass().add("input-error");
                }

                case EMPTY_PASSWORD -> {
                    errorLabel.setText("Please enter your password!");
                    errorLabel.setVisible(true);
                    passwordField.getStyleClass().add("input-error");
                    passwordPlain.getStyleClass().add("input-error");
                }

                case INVALID_PASSWORD -> {
                    errorLabel.setText("Invalid username or password");
                    errorLabel.setVisible(true);
                }

                case USER_NOT_FOUND -> {
                    errorLabel.setText(String.format("User with username (or email) %s not found", username));
                    errorLabel.setVisible(true);
                }
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
            new LoginView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(LoginView.TestApp.class, args);
    }
}
