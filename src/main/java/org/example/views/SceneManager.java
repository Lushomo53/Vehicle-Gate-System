package org.example.views;

import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {
    private static Stage stage;
    private static Scene scene;

    public static void init(Stage primaryStage) {
        if (stage != null) return;
        stage = primaryStage;
        scene = new Scene(new StackPane(), 1000, 700);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles/app.css")).toExternalForm());
        } catch (Exception ignored) {}

        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setResizable(true);
    }

    public static void setScene(Parent root, String title) {
        if (stage == null) throw new IllegalStateException("SceneManager uninitialised");
        stage.setTitle(title);
        scene.setRoot(root);
        stage.show();
    }
}
