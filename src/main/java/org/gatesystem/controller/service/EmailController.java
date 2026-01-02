package org.gatesystem.controller.service;

import jakarta.mail.MessagingException;
import org.gatesystem.util.HTMLTemplateLoader;
import org.gatesystem.util.OTPGenerator;

import java.util.Map;

public class EmailController {
    public static final String APP_LOGO_URL = "https://github.com/Lushomo53/Vehicle-Gate-System/blob/master/src/main/resources/images/gate_system_logo.jpeg?raw=true";

    public void sendOTpEmail() {
        Thread emailThread = new Thread(() -> {
            EmailService emailService = new EmailService(
                    "smtp.gmail.com",
                    EmailService.TLS_PORT_NUMBER,
                    EmailService.SYSTEM_USERNAME,
                    EmailService.PASSWORD
            );

            SessionManager.currentSession.setOtp(OTPGenerator.generate(5));
            Map<String, String> values = Map.of("OTP_CODE", SessionManager.currentSession.getOtp().toString());
            String template = HTMLTemplateLoader.load("/templates/otp_template.html", values);

            try {
                emailService.sendEmail(
                        SessionManager.currentSession.getCurrentUser().getEmail(),
                        "Email Verification",
                        template,
                        true
                );
                System.out.println("Email Sent");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

        emailThread.start();
    }

    public void sendRegistryEmail(String firstName, String username, String email, String password) {
        Thread emailThread = new Thread(() -> {
            EmailService emailService = new EmailService(
                    "smtp.gmail.com",
                    EmailService.TLS_PORT_NUMBER,
                    EmailService.SYSTEM_USERNAME,
                    EmailService.PASSWORD
            );

            Map<String, String> values = Map.of(
                    "FIRST_NAME", firstName,
                    "USERNAME", username,
                    "TEMP_PASSWORD", password,
                    "APP_LOGO_URL", APP_LOGO_URL
            );

            String template = HTMLTemplateLoader.load("/templates/welcome_user.html", values);

            try {
                emailService.sendEmail(email, "Welcome", template, true);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });

        emailThread.start();
    }

}
