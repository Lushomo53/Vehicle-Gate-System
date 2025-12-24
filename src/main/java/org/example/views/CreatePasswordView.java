package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.controller.SignInStatus;
import org.example.controller.UserController;
import org.example.controller.service.SessionManager;
import org.example.util.PasswordUtil;

public class CreatePasswordView implements Screen {
    private Parent root;

    @Override
    public Parent getRoot() { return root; }

    public void show(Stage stage) {
        root = build();
        SceneManager.init(stage);
        SceneManager.setScene(root, "Create Password");
    }

    private Parent build() {
        UserController controller = new UserController();

        Label title = new Label("Create Password");
        title.getStyleClass().add("label-app-title");

        Label prompt = new Label("Password");
        prompt.setFont(new Font(12));

        PasswordField field = new PasswordField();
        field.getStyleClass().add("input-field");
        field.setPromptText("Create a strong password");

        TextField passwordPlain = new TextField();
        passwordPlain.getStyleClass().add("input-field");
        passwordPlain.setPromptText("Create a strong password");
        passwordPlain.textProperty().bindBidirectional(field.textProperty());

        Label instructions = new Label("""
                Create a strong password with:
                . at least eight characters
                . a mixture of uppercase, lowercase, digits\s
                and special characters
               \s""");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        CheckBox showPassword = new CheckBox("Show");

        field.managedProperty().bind(showPassword.selectedProperty().not());
        field.visibleProperty().bind(showPassword.selectedProperty().not());
        passwordPlain.managedProperty().bind(showPassword.selectedProperty());
        passwordPlain.visibleProperty().bind(showPassword.selectedProperty());

        Button createBtn = new Button("Create");
        createBtn.getStyleClass().add("button-primary");

        VBox form = new VBox(8, field, passwordPlain, instructions, showPassword);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(10));
        form.setFillWidth(false);
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).setMaxWidth(320);
                ((TextField) node).setPrefWidth(320);
            }
        });

        VBox cardContent = new VBox(16, title, form, errorLabel, createBtn);
        cardContent.setPadding(new Insets(16));
        cardContent.setAlignment(Pos.CENTER);

        VBox card = new VBox(14, cardContent);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.setPadding(new Insets(16));
        card.getStyleClass().add("login-card");

        VBox root = new VBox(16, card);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        passwordPlain.setOnAction(_-> createBtn.fire());
        field.setOnAction(_-> createBtn.fire());

        createBtn.setOnAction(_ -> {
            String password = field.isVisible() ? field.getText().trim() : passwordPlain.getText().trim();

            SignInStatus status = controller.createPassword(password);

            switch (status) {
                case STRONG_PASSWORD -> {
                    SessionManager.currentSession.getCurrentUser().setPassword(PasswordUtil.hashPassword(password));
                    OTPVerificationView otpVV = new OTPVerificationView();
                    SessionManager.currentSession.switchScreen(otpVV);
                    otpVV.show((Stage) root.getScene().getWindow());
                }

                case PASSWORD_MISSING_DIGITS -> {
                    errorLabel.setText("Your password must have at least one digit");
                    errorLabel.setVisible(true);
                    passwordPlain.getStyleClass().add("input-error");
                    field.getStyleClass().add("input-error");
                    if (field.isVisible()) field.requestFocus();
                    else passwordPlain.requestFocus();
                }

                case PASSWORD_MISSING_LOWERCASE_CHARACTER -> {
                    errorLabel.setText("Your password must have at least one lowercase letter");
                    errorLabel.setVisible(true);
                    passwordPlain.getStyleClass().add("input-error");
                    field.getStyleClass().add("input-error");
                    if (field.isVisible()) field.requestFocus();
                    else passwordPlain.requestFocus();
                }

                case PASSWORD_MISSING_UPPERCASE_CHARACTER -> {
                    errorLabel.setText("Your password must have at least one uppercase letter");
                    errorLabel.setVisible(true);
                    passwordPlain.getStyleClass().add("input-error");
                    field.getStyleClass().add("input-error");
                    if (field.isVisible()) field.requestFocus();
                    else passwordPlain.requestFocus();
                }

                case PASSWORD_MISSING_SPECIAL_CHARACTERS -> {
                    errorLabel.setText("Your password must have at least special character");
                    errorLabel.setVisible(true);
                    passwordPlain.getStyleClass().add("input-error");
                    field.getStyleClass().add("input-error");
                    if (field.isVisible()) field.requestFocus();
                    else passwordPlain.requestFocus();
                }

                case PASSWORD_TOO_SHORT -> {
                    errorLabel.setText("Your password must contain at least eight characters");
                    errorLabel.setVisible(true);
                    passwordPlain.getStyleClass().add("input-error");
                    field.getStyleClass().add("input-error");
                    if (field.isVisible()) field.requestFocus();
                    else passwordPlain.requestFocus();
                }
            }
        });

        return root;
    }
}
