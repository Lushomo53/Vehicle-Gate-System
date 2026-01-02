package org.gatesystem;

import javafx.application.Application;
import javafx.stage.Stage;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.views.InitialScreen;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        InitialScreen initialScreen = new InitialScreen();
        SessionManager.currentSession = new SessionManager(initialScreen);
        initialScreen.show(primaryStage);
    }
    public static void main(String[] args) {
        launch(args);
    }
}