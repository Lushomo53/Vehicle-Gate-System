package org.example.controller.service;

import jakarta.mail.MessagingException;
import org.example.util.HTMLTemplateLoader;
import org.example.util.OTPGenerator;

import java.util.Map;

public class EmailController {
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
}
