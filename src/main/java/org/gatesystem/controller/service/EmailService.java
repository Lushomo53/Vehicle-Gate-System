package org.gatesystem.controller.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.gatesystem.util.EnvLoader;

import java.util.Properties;

public class EmailService {
    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;

    public static final String SYSTEM_USERNAME = EnvLoader.get("EMAIL_USER");
    public static final String PASSWORD = EnvLoader.get("EMAIL_PASSWORD");
    public static final int TLS_PORT_NUMBER = Integer.parseInt(EnvLoader.get("EMAIL_PORT"));

    public EmailService(String smtpHost, int smtpPort, String username, String password) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    private Session createSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String recipient, String subject, String content, boolean isHtml) throws MessagingException{
        Session session = createSession();
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);

        if (isHtml) {
            message.setContent(content, "text/html; charset=UTF-8");
        } else {
            message.setText(content);
        }

        Transport.send(message);
    }

    public static void main(String[] args) {
        EmailService emailService = new EmailService(
                "smtp.gmail.com",
                587,
                "lungolushomo21@gmail.com",
                "vkbzxpvyleqjjywk"
        );

        try {
            emailService.sendEmail(
                    "blantiresavgelevel21@gmail.com",
                    "Test OTP",
                    "<h2>Your OTP is 123456</h2>",
                    true
            );
            System.out.println("Email sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
