package org.gatesystem.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import org.gatesystem.controller.SignInStatus;
import org.gatesystem.controller.UserController;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.User;
import org.gatesystem.util.UIUtil;
import org.gatesystem.views.components.BackPane;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterUserView implements Screen {
    private Parent root;
    private final String title = "Register User";

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
        ImageView iconView = new ImageView();

        try {
            Image icon = new Image(Objects.requireNonNull(RegisterUserView.class.getResourceAsStream("/images/user_icon.jpg")));
            iconView.setImage(icon);
            iconView.setFitWidth(120);
            iconView.setFitHeight(120);
            iconView.setPreserveRatio(true);
            iconView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("Failed to load image");
        }

        Label firstNamePrompt = new Label("First Name");
        firstNamePrompt.setFont(new Font(12));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter First Name");
        firstNameField.getStyleClass().add("input-field");

        Label lastNamePrompt = new Label("Last Name");
        lastNamePrompt.setFont(new Font(12));

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter Last Name");
        lastNameField.getStyleClass().add("input-field");

        Label nrcPrompt = new Label("NRC");
        nrcPrompt.setFont(new Font(12));

        TextField nrcField = new TextField();
        nrcField.setPromptText("XXXXXX/XX/X");
        nrcField.getStyleClass().add("input-field");

        Label userNamePrompt = new Label("Username");
        userNamePrompt.setFont(new Font(12));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Create Username");
        usernameField.getStyleClass().add("input-field");

        Label emailPrompt = new Label("Email");
        emailPrompt.setFont(new Font(12));

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");
        emailField.getStyleClass().add("input-field");

        Label roleLabel = new Label("User Role");
        roleLabel.setFont(new Font(12));

        ComboBox<String> roleOptions = new ComboBox<>();
        roleOptions.setPromptText("ROLE");
        roleOptions.getItems().addAll("ADMIN", "GUARD", "GUEST");
        roleOptions.getStyleClass().add("ordinary-combo-box");

        VBox form = new VBox(8,
                firstNamePrompt,
                firstNameField,
                lastNamePrompt,
                lastNameField,
                nrcPrompt,
                nrcField,
                userNamePrompt,
                usernameField,
                emailPrompt,
                emailField,
                roleLabel,
                roleOptions);

        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(10));
        form.setFillWidth(false);
        form.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).setPrefWidth(320);
                ((TextField) node).setMaxWidth(320);
            }
        });

        Button registerBtn = new Button("Register");
        registerBtn.setAlignment(Pos.CENTER);
        registerBtn.getStyleClass().add("button-primary");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        VBox cardContent = new VBox(10, iconView, form, registerBtn, errorLabel);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(10));

        VBox card = new VBox(14, cardContent);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox centerContent = new VBox(12, card);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(16));

        BorderPane root = new BorderPane();
        root.setTop(new BackPane());
        root.setCenter(centerContent);

        firstNameField.setOnAction(e -> lastNameField.requestFocus());
        lastNameField.setOnAction(e -> nrcField.requestFocus());
        nrcField.setOnAction(e -> usernameField.requestFocus());
        usernameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> roleOptions.requestFocus());

        UserController controller = new UserController();

        registerBtn.setOnAction(_-> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String nrc = nrcField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            User.Role role;

            form.getChildren().forEach(node -> {
                if (node instanceof TextField) {
                    node.getStyleClass().remove("input-error");
                }
            });
            errorLabel.setVisible(false);

            try {
                role = User.Role.valueOf(roleOptions.getSelectionModel().getSelectedItem());
            } catch (IllegalArgumentException e) {
                UIUtil.displayError("Please select a role", null, errorLabel, false);
                roleOptions.requestFocus();
                return;
            }

            SignInStatus status = controller.registerUser(firstName, lastName, nrc, username, email, role);

            switch (status) {
                case SIGN_IN_SUCCESSFUL -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Registration Success");
                    alert.setContentText("User " + username + " registered successfully");
                    alert.showAndWait();
                    SessionManager.currentSession.switchToPreviousScreen();
                }

                case EMPTY_FIELD -> {
                    AtomicBoolean first = new AtomicBoolean(true);
                    form.getChildren().forEach(node -> {
                        if (node instanceof TextField && ((TextField) node).getText().trim().isEmpty()) {
                            node.getStyleClass().add("input-error");

                            if (first.get()) {
                                node.requestFocus();
                                first.set(false);
                            }
                        }

                        errorLabel.setText("Please fill in all fields available");
                        errorLabel.setVisible(true);
                    });
                }

                case INVALID_NRC -> UIUtil.displayError("Please enter a valid NRC number", nrcField, errorLabel, true);
                case INVALID_EMAIL -> UIUtil.displayError("Please enter a valid email address", emailField, errorLabel, true);
                case USERNAME_ALREADY_EXISTS -> UIUtil.displayError("An account with username \"" + username + "\" already exists.", usernameField, errorLabel, true);
                case EMAIL_ALREADY_EXISTS -> UIUtil.displayError("An account with the email \"" + email + "\" already exists.", emailField, errorLabel, true);
            }
        });

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new RegisterUserView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
