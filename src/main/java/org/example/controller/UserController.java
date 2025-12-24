package org.example.controller;

import org.example.controller.service.SessionManager;
import org.example.model.User;
import org.example.repo.UserRepository;
import org.example.util.PasswordUtil;
import org.example.util.RegexPatterns;

public class UserController {
    private final UserRepository repo;

    public UserController() {
        repo = new UserRepository();
    }

    public LoginStatus authenticateUser(String username, String enteredPassword) {
        if (username.isEmpty()) return LoginStatus.EMPTY_USERNAME;
        if (enteredPassword.isEmpty()) return LoginStatus.EMPTY_PASSWORD;

        User user = repo.findByUsername(username);
        if (user == null) user = repo.findByEmail(username);

        if (user == null) return LoginStatus.USER_NOT_FOUND;

        if (PasswordUtil.checkPassword(enteredPassword, user.getPasswordHash())) {
            SessionManager.currentSession.switchUser(user);
            return LoginStatus.LOGIN_SUCCESSFUL;
        } else {
            return LoginStatus.INVALID_PASSWORD;
        }
    }

    public SignInStatus signIn(String firstName, String lastName, String nrc, String username, String email) {
        if (firstName.isEmpty() || lastName.isEmpty() || nrc.isEmpty() || username.isEmpty() || email.isEmpty()) {
            return SignInStatus.EMPTY_FIELD;
        }

        if (!RegexPatterns.NRC_PATTERN.matcher(nrc).matches()) return SignInStatus.INVALID_NRC;
        if (!RegexPatterns.EMAIL_PATTERN.matcher(email).matches()) return SignInStatus.INVALID_EMAIL;

        if (repo.usernameExists(username)) return SignInStatus.USERNAME_ALREADY_EXISTS;
        if (repo.emailExists(email)) return SignInStatus.EMAIL_ALREADY_EXISTS;

        return SignInStatus.SIGN_IN_SUCCESSFUL;
    }

    public SignInStatus createPassword(String password) {
        if (password.length() < 8) return SignInStatus.PASSWORD_TOO_SHORT;
        if (!RegexPatterns.AT_LEAST_ONE_LOWERCASE.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_LOWERCASE_CHARACTER;
        if (!RegexPatterns.AT_LEAST_ONE_UPPERCASE.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_UPPERCASE_CHARACTER;
        if (!RegexPatterns.AT_LEAST_ONE_SPECIAL.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_SPECIAL_CHARACTERS;
        return SignInStatus.STRONG_PASSWORD;
    }

    public void registerUser(User user) {
        repo.addUser(user);
    }
}
