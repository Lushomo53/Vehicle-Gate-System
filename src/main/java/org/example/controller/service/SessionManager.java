package org.example.controller.service;

import javafx.scene.Parent;
import org.example.model.User;
import org.example.util.OTP;
import org.example.views.InitialScreen;
import org.example.views.Screen;

import java.util.Stack;

public class SessionManager {
    private User currentUser;
    private OTP otp;
    private final Stack<Screen> screenStack;
    public static SessionManager currentSession;

    static {
        Screen initalScreen = new InitialScreen();
        currentSession = new SessionManager(initalScreen);
    }

    public SessionManager(Screen initialScreen) {
        screenStack = new Stack<>();
        screenStack.push(initialScreen);
        currentSession = this;
    }

    public User getCurrentUser() { return currentUser; }
    public OTP getOtp() { return otp; }
    public Screen getRecentScreen() { return screenStack.pop(); }
    public void clearOtp() { otp = null; }
    public void setOtp(OTP otp) { this.otp = otp; }
    public void switchUser(User user) { this.currentUser = user; }
    public void switchScreen(Screen newScreen) { screenStack.push(newScreen); }
}
