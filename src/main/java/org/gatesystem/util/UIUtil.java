package org.gatesystem.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class UIUtil {
    public static void displayError(String message, TextField field, Label errorLabel, boolean highlightField) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        if (highlightField) {
            field.getStyleClass().add("input-error");
            field.requestFocus();
        }
    }
}
