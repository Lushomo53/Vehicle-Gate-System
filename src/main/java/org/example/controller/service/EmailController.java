package org.example.controller.service;

public class EmailController {
    public void sendOTpEmail() {
        Thread emailThread = new Thread(() -> {
            EmailService emailService = new EmailService(
                    "smtp.gmail.com",
                    EmailService.TLS_PORT_NUMBER,
                    EmailService.SYSTEM_USERNAME,
                    EmailService.PASSWORD
            );

            String template = "";
        });
    }
}
