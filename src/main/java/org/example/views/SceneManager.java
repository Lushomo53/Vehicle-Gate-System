package org.example.views;

import javafx.scene.*;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {
    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void setScene(Parent root, String title) {
        if (stage == null) throw new IllegalStateException("SceneManager uninitialised");

        Scene scene = new Scene(root);

        try {
            scene.getStylesheets().add(Objects.requireNonNull(SceneManager.class.getResource("/styles/app.css")).toExternalForm());
        } catch (Exception ignored) {}

        stage.setScene(scene);
        stage.setTitle(title);
        stage.sizeToScene();
        stage.show();
    }
}
