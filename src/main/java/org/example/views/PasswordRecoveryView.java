package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.*;


public class PasswordRecoveryView {
    public void show(Stage stage) {
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Password Recovery");
    }

    public Parent build() {
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

        VBox root = new VBox(10, card);
        root.setPadding(new Insets(16));
        root.setAlignment(Pos.CENTER);

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
