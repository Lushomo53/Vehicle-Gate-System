package org.gatesystem.views.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.gatesystem.controller.service.SessionManager;

public class BackPane extends HBox {
    public BackPane() {
        Button backBtn = new Button("← Back");
        backBtn.getStyleClass().add("back-button");
        backBtn.setOnAction(_ -> {
            SessionManager.currentSession.switchToPreviousScreen();
        });

        this.getChildren().add(backBtn);
        this.setPadding(new Insets(8, 12, 0, 12));
        this.setAlignment(Pos.CENTER_LEFT);
    }
}
