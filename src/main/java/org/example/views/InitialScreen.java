package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

public class InitialScreen {
    public static void show(Stage stage) {
        stage.getIcons().add(
                new Image(Objects.requireNonNull(InitialScreen.class.getResourceAsStream("/images/gate_system_logo.jpeg")))
        );
        SceneManager.init(stage);
        SceneManager.setScene(build(), "Welcome");
    }

    private static Parent build() {
        ImageView logoView = new ImageView();

        try {
            Image logo = new Image(
                    Objects.requireNonNull(InitialScreen.class.getResourceAsStream("/images/gate_system_logo.jpeg"))
            );
            logoView.setImage(logo);
            logoView.setFitWidth(120);
            logoView.setFitHeight(120);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("Image not found");
        }

        Label appTitle = new Label("Vehicle Gate System");
        appTitle.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #333333;"
        );

        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");

        loginBtn.setPrefWidth(140);
        signUpBtn.setPrefWidth(140);

        HBox buttons = new HBox(12, loginBtn, signUpBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(20);

        root.setPadding(new Insets(20));
        root.getStylesheets().add(Objects.requireNonNull(InitialScreen.class.getResource("/styles/app.css")).toExternalForm());
        root.setAlignment(Pos.CENTER);

        appTitle.getStyleClass().add("label-app-title");
        loginBtn.getStyleClass().add("button-primary");
        signUpBtn.getStyleClass().add("button-secondary");

        root.getChildren().addAll(logoView, appTitle, buttons);

        loginBtn.setOnAction(e -> LoginView.show((Stage) root.getScene().getWindow()));
        signUpBtn.setOnAction(e -> SignUpView.show((Stage) root.getScene().getWindow()));

        return root;
    }

    public static void main(String[] args) {
        javafx.application.Application.launch(TestApp.class, args);
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            InitialScreen.show(primaryStage);
        }
    }
}
