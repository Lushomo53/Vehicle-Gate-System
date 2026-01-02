package org.gatesystem.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.gatesystem.controller.VerificationController;
import org.gatesystem.controller.VerificationPurpose;
import org.gatesystem.controller.VerificationStatus;
import org.gatesystem.controller.service.EmailController;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.views.components.OTPInputField;

import java.util.concurrent.atomic.AtomicInteger;

public class OTPVerificationView implements Screen {
    private Parent root;
    private final VerificationPurpose purpose;
    private final String title = "Verification";
    private static final int RESEND_DELAY = 60;

    public OTPVerificationView(VerificationPurpose purpose) {
        this.purpose = purpose;
    }

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
        EmailController emailController = new EmailController();
        emailController.sendOTpEmail();

        VerificationController verificationController = new VerificationController();

        Label title = new Label("Verification");
        title.getStyleClass().add("label-app-title");

        Label info = new Label("Enter the code sent to: " + SessionManager.currentSession.getCurrentUser().getEmail());

        OTPInputField otpField = new OTPInputField(6);
        otpField.style("otp-field");
        otpField.setAlignment(Pos.CENTER);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.getStyleClass().add("button-primary");

        Hyperlink resendLink = new Hyperlink("Resend Code");
        resendLink.getStyleClass().add("reset-link");
        resendLink.setDisable(true);

        Label timerLabel = new Label();
        timerLabel.setText("Resend in " + RESEND_DELAY + " seconds");
        timerLabel.getStyleClass().add("timer-label");

        Timeline timeline = new Timeline();
        timeline.setCycleCount(RESEND_DELAY);
        AtomicInteger timeLeft = new AtomicInteger(RESEND_DELAY);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            timeLeft.set(timeLeft.get() - 1);
            timerLabel.setText("Resend in " + timeLeft.get() + " seconds");

            if (timeLeft.get() <= 0) {
                timerLabel.setText("");
                resendLink.setDisable(false);
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        otpField.filledProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) confirmBtn.fire();
        }));

        Label errorLabel = new Label();
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("helper-error");

        VBox card = new VBox(10, title, info, otpField, confirmBtn, errorLabel, resendLink, timerLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMaxWidth(400);
        card.getStyleClass().add("login-card");

        VBox root =new VBox(10, card);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        resendLink.setOnAction(_-> {
            emailController.sendOTpEmail();
            resendLink.setDisable(true);
            timeLeft.set(RESEND_DELAY);
            timeline.playFromStart();
        });

        confirmBtn.setOnAction(_-> {
            otpField.getChildren().forEach(node -> node.getStyleClass().remove("input-error"));
            errorLabel.setVisible(false);

            String otp = otpField.getOTP();
            VerificationStatus status = verificationController.verify(otp, purpose);

            switch (status) {
                case VERIFIED -> {
                    if (purpose == VerificationPurpose.REGISTER) {
                        DashboardView dashboardView = new DashboardView();
                        SessionManager.currentSession.switchScreen(dashboardView);
                        dashboardView.show((Stage) root.getScene().getWindow());
                    } else if (purpose == VerificationPurpose.RESET_PASSWORD) {
                        CreatePasswordView createPasswordView = new CreatePasswordView(purpose);
                        SessionManager.currentSession.switchScreen(createPasswordView);
                        createPasswordView.show((Stage) root.getScene().getWindow());
                    }
                }

                case EMPTY_FIELD -> {
                    otpField.style("input-error");
                    errorLabel.setText("Please fill in otp code field");
                    errorLabel.setVisible(true);
                    otpField.clear();
                    otpField.getChildren().getFirst().requestFocus();
                }

                case INVALID_OTP -> {
                    otpField.style("input-error");
                    errorLabel.setText("The code you have entered is either invalid or expired");
                    errorLabel.setVisible(true);
                    otpField.clear();
                    otpField.getChildren().getFirst().requestFocus();
                }
            }
        });

        return root;
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage stage) {
            new OTPVerificationView(VerificationPurpose.REGISTER).show(stage);
        }
    }

    public static void main(String[] args) {
        Application.launch(TestApp.class, args);
    }
}
