package org.example.views.components;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class OTPInputField extends HBox {
    private final int length;
    private final List<TextField> fields = new ArrayList<>();
    private final BooleanProperty filled = new SimpleBooleanProperty(false);

    public OTPInputField(int length) {
        this.length = length;
        setSpacing(10);
        initFields();
        setUpPasteHandling();
    }

    private void initFields() {
        for (int i = 0; i < length; i++) {
            TextField field = createDigitField(i);
            fields.add(field);
            getChildren().add(field);
        }
    }

    private TextField createDigitField(int index) {
        TextField field = new TextField();

        field.setTextFormatter(new TextFormatter<String>(change -> {
            String text = change.getControlNewText();
            if (!text.matches("\\d?")) return null;
            return change;
        }));

        field.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && index < length - 1) fields.get(index + 1).requestFocus();
            updateFilledState();
        }));

        field.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.BACK_SPACE && field.getText().isEmpty()) {
                if (index > 0) {
                    TextField prev = fields.get(index - 1);
                    prev.requestFocus();
                    prev.clear();
                }
            }
        });

        return field;
    }

    private void updateFilledState() {
        boolean isFilled = fields.stream().allMatch(field -> field.getText().length() == 1);
        filled.set(isFilled);
    }

    private void setUpPasteHandling() {
        this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.V) {
                String paste = Clipboard.getSystemClipboard().getString();
                if (paste != null && paste.matches("\\d+")) {
                    fillFromPaste(paste);
                }
                e.consume();
            }
        });
    }

    private void fillFromPaste(String paste) {
        for (int i= 0; i < length; i++) {
            if (i < paste.length()) {
                fields.get(i).setText(String.valueOf(paste.charAt(i)));
            } else {
                fields.get(i).clear();
            }
        }

        int focusIndex = Math.min(paste.length(), length - 1);
        if (focusIndex >= 0) fields.get(focusIndex).requestFocus();
    }

    public String getOTP() {
        StringBuilder otp = new StringBuilder();
        fields.forEach(field -> {
            otp.append(field.getText());
        });
        return otp.toString();
    }

    public BooleanProperty filledProperty() { return filled; }

    public void style(String URI) {
        fields.forEach(f -> f.getStyleClass().add(URI));
    }
}
