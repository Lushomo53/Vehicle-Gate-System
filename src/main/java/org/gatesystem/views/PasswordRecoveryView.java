package org.gatesystem.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;
import org.gatesystem.controller.RecoveryStatus;
import org.gatesystem.controller.UserController;
import org.gatesystem.controller.VerificationPurpose;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.views.components.BackPane;

import static org.gatesystem.util.UIUtil.displayError;


public class PasswordRecoveryView implements Screen {
    private Parent root;
    private final String title = "Password Recovery";

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

        Label title = new Label("Recover Password");
        title.getStyleClass().add("label-app-title");

        Label usernamePrompt = new Label("Username");
        usernamePrompt.setFont(new Font(12));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Your Username");
        usernameField.getStyleClass().add("input-field");

        Label emailPrompt = new Label("Email");
        emailPrompt.setFont(new Font(12));

        TextField emailField = new TextField();
        emailField.setPromptText("username@domain.com");
        emailField.getStyleClass().add("input-field");

        VBox form = new VBox(8, usernamePrompt, usernameField, emailPrompt, emailField);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(10));
        form.setFillWidth(false);
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).setPrefWidth(320);
                ((TextField) node).setMaxWidth(320);
            }
        });

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("helper-error");

        Button recoverBtn = new Button("Recover");
        recoverBtn.getStyleClass().add("button-primary");

        VBox cardContent = new VBox(10, title, form, errorLabel, recoverBtn);
        cardContent.setPadding(new Insets(10));
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(16, cardContent);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);

        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox centerContent = new VBox(10, card);
        centerContent.setPadding(new Insets(16));
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(new BackPane());
        root.setCenter(centerContent);

        usernameField.setOnAction(_-> emailField.requestFocus());
        emailField.setOnAction(_-> recoverBtn.fire());

        recoverBtn.setOnAction(_-> {
            errorLabel.setVisible(false);
            form.getChildren().forEach(node -> {
                if (node instanceof TextField) {
                    node.getStyleClass().remove("input-error");
                }
            });

            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();

            RecoveryStatus status = controller.recoverAccount(username, email);
            System.out.println(status);

            switch (status) {
                case EMPTY_USERNAME -> displayError("Please enter your username", usernameField, errorLabel, true);
                case EMPTY_EMAIL -> displayError("Please enter your email", emailField, errorLabel, true);
                case INVALID_EMAIL -> displayError("Please enter a valid email address, e.g. username@domain.com", emailField, errorLabel, true);
                case USER_NOT_FOUND -> displayError("No user found for the username or email entered", null, errorLabel, false);
                case USER_FOUND -> {
                    OTPVerificationView otpView = new OTPVerificationView(VerificationPurpose.RESET_PASSWORD);
                    SessionManager.currentSession.switchScreen(otpView);
                    otpView.show((Stage) root.getScene().getWindow());
                }
            }
        });

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new PasswordRecoveryView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
