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
import org.gatesystem.controller.VerificationPurpose;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.User;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUpView implements Screen {
    private final String title = "Sign Up";
    private Parent root;

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
        UserController controller = new UserController();

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

        Label nrcPrompt = new Label("Prompt");
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
                emailField
                );

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

        Label loginLinkLabel = new Label("Already have an account?");
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.getStyleClass().add("reset-link");
        HBox loginLinkLine = new HBox(2, loginLinkLabel, loginLink);
        loginLinkLine.setAlignment(Pos.BASELINE_CENTER);

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("helper-error");
        errorLabel.setVisible(false);

        firstNameField.setOnAction(_ -> lastNameField.requestFocus());
        lastNameField.setOnAction(_ -> nrcField.requestFocus());
        nrcField.setOnAction(_ -> usernameField.requestFocus());
        usernameField.setOnAction(_ -> emailField.requestFocus());
        emailField.setOnAction(_ -> registerBtn.fire());


        VBox cardContent = new VBox(10, iconView, form, registerBtn, errorLabel, loginLinkLine);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(10));

        VBox card = new VBox(14, cardContent);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(400);
        card.getStyleClass().add("login-card");

        VBox root = new VBox(12, card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(16));

        registerBtn.setOnAction(_ -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String nrc = nrcField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();

            form.getChildren().forEach(node -> {
                if (node instanceof TextField) {
                    node.getStyleClass().remove("input-error");
                }
            });
            errorLabel.setVisible(false);

            SignInStatus status = controller.signIn(firstName, lastName, nrc, username, email);

            switch (status) {
                case SIGN_IN_SUCCESSFUL -> {
                    SessionManager.currentSession.switchUser(new User(username, email, User.Role.GUEST, firstName, lastName, nrc));
                    CreatePasswordView createPasswordView = new CreatePasswordView(VerificationPurpose.REGISTER);
                    SessionManager.currentSession.switchScreen(createPasswordView);
                    createPasswordView.show((Stage) root.getScene().getWindow());
                }

                case EMPTY_FIELD -> {
                    AtomicBoolean isFirst = new AtomicBoolean(true);

                    form.getChildren().forEach(node -> {
                        if (node instanceof TextField) {
                            if (((TextField) node).getText().isEmpty()) {
                                node.getStyleClass().add("input-error");

                                if (isFirst.get()) {
                                    node.requestFocus();
                                    isFirst.set(false);
                                }
                            }
                        }
                    });
                    errorLabel.setText("Please fill in all fields available");
                    errorLabel.setVisible(true);
                }

                case INVALID_NRC -> {
                    nrcField.getStyleClass().add("input-error");
                    nrcField.requestFocus();
                    errorLabel.setText("Please enter a valid NRC number!");
                    errorLabel.setVisible(true);
                }

                case INVALID_EMAIL -> {
                    emailField.getStyleClass().add("input-error");
                    emailField.requestFocus();
                    errorLabel.setText("Please enter a valid email!");
                    errorLabel.setVisible(true);
                }

                case USERNAME_ALREADY_EXISTS -> {
                    usernameField.getStyleClass().add("input-error");
                    usernameField.requestFocus();
                    errorLabel.setText("An account with username \"" + username + "\" already exists.");
                    errorLabel.setVisible(true);
                }

                case EMAIL_ALREADY_EXISTS -> {
                    emailField.getStyleClass().add("input-error");
                    emailField.requestFocus();
                    errorLabel.setText("An account was already registered with the email \"" + email + "\".");
                    errorLabel.setVisible(true);
                }
            }
        });

        loginLink.setOnAction(_ -> {
            LoginView loginView = new LoginView();
            SessionManager.currentSession.switchScreen(loginView);
            loginView.show((Stage) root.getScene().getWindow());
        });

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new SignUpView().show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}

