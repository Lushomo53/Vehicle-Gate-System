package org.gatesystem.controller.service;

import org.gatesystem.model.User;
import org.gatesystem.util.OTP;
import org.gatesystem.views.InitialScreen;
import org.gatesystem.views.SceneManager;
import org.gatesystem.views.Screen;

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
    public Screen getPreviousScreen() { return screenStack.get(screenStack.size() - 2); }
    public OTP getOtp() { return otp; }
    public void clearOtp() { otp = null; }
    public void setOtp(OTP otp) { this.otp = otp; }
    public void switchUser(User user) { this.currentUser = user; }
    public void switchScreen(Screen newScreen) {
        screenStack.push(newScreen);
        if (screenStack.size() > 3) screenStack.removeFirst();
    }
    public void switchToPreviousScreen() {
        screenStack.pop();
        Screen screen = screenStack.peek();
        SceneManager.setScene(screen.getRoot(), screen.getTitle());
    }
    public void clear() {
        screenStack.clear();
        currentUser = null;
        clearOtp();
    }
}
