package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;

import java.util.Objects;


public class RegisterUserView {
    public void show(Stage stage) {
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Register User");
    }

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

        Label nrcPrompt = new Label("Prompt");
        nrcPrompt.setFont(new Font(12));

        TextField nrcField = new TextField();
        nrcField.setPromptText("XXXXX/XX/X");
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

        firstNameField.setOnAction(e -> lastNameField.requestFocus());
        lastNameField.setOnAction(e -> nrcField.requestFocus());
        nrcField.setOnAction(e -> usernameField.requestFocus());
        usernameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> roleOptions.requestFocus());

        VBox cardContent = new VBox(10, iconView, form, registerBtn, errorLabel);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(10));

        VBox card = new VBox(14, cardContent);
        card.setPadding(new Insets(16));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox root = new VBox(12, card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(16));

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
