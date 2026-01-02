package org.gatesystem.controller;

import org.gatesystem.controller.service.EmailController;
import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.model.User;
import org.gatesystem.repo.UserRepository;
import org.gatesystem.util.PasswordGenerator;
import org.gatesystem.util.PasswordUtil;
import org.gatesystem.util.RegexPatterns;

public class UserController {
    private final UserRepository repo;

    public UserController() {
        repo = new UserRepository();
    }

    public LoginStatus authenticateUser(String username, String enteredPassword) {
        if (username.isEmpty()) return LoginStatus.EMPTY_USERNAME;
        if (enteredPassword.isEmpty()) return LoginStatus.EMPTY_PASSWORD;

        User user = repo.findByUsernameOrEmail(username, username);
        if (user == null) return LoginStatus.USER_NOT_FOUND;

        if (PasswordUtil.checkPassword(enteredPassword, user.getPasswordHash())) {
            SessionManager.currentSession.switchUser(user);
            return LoginStatus.LOGIN_SUCCESSFUL;
        } else {
            return LoginStatus.INVALID_PASSWORD;
        }
    }

    public RecoveryStatus recoverAccount(String username, String email) {
        if (username.isEmpty()) return RecoveryStatus.EMPTY_USERNAME;
        if (email.isEmpty()) return RecoveryStatus.EMPTY_EMAIL;
        if (!RegexPatterns.EMAIL_PATTERN.matcher(email).matches()) return RecoveryStatus.INVALID_EMAIL;

        User user = repo.findByUsernameOrEmail(username, email);
        if (user == null) return RecoveryStatus.USER_NOT_FOUND;
        else {
            SessionManager.currentSession.switchUser(user);
            return RecoveryStatus.USER_FOUND;
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

    public SignInStatus registerUser(String firstName, String lastName, String nrc, String username, String email, User.Role role) {
        if (firstName.isEmpty() || lastName.isEmpty() || nrc.isEmpty() || username.isEmpty() || email.isEmpty()) {
            return SignInStatus.EMPTY_FIELD;
        }

        if (!RegexPatterns.NRC_PATTERN.matcher(nrc).matches()) return SignInStatus.INVALID_NRC;
        if (!RegexPatterns.EMAIL_PATTERN.matcher(email).matches()) return SignInStatus.INVALID_EMAIL;

        if (repo.usernameExists(username)) return SignInStatus.USERNAME_ALREADY_EXISTS;
        if (repo.emailExists(email)) return SignInStatus.EMAIL_ALREADY_EXISTS;

        User newUser = new User(username, email, role, firstName, lastName, nrc);

        String password = PasswordGenerator.generate();
        newUser.setPassword(PasswordUtil.hashPassword(password));
        repo.addUser(newUser);

        EmailController emailController = new EmailController();
        emailController.sendRegistryEmail(firstName, username, email, password);
        return SignInStatus.SIGN_IN_SUCCESSFUL;
    }

    public SignInStatus createPassword(String password) {
        if (password.length() < 8) return SignInStatus.PASSWORD_TOO_SHORT;
        if (!RegexPatterns.AT_LEAST_ONE_LOWERCASE.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_LOWERCASE_CHARACTER;
        if (!RegexPatterns.AT_LEAST_ONE_UPPERCASE.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_UPPERCASE_CHARACTER;
        if (!RegexPatterns.AT_LEAST_ONE_SPECIAL.matcher(password).matches()) return SignInStatus.PASSWORD_MISSING_SPECIAL_CHARACTERS;
        SessionManager.currentSession.getCurrentUser().setPassword(PasswordUtil.hashPassword(password));
        return SignInStatus.STRONG_PASSWORD;
    }

    public ResetPasswordStatus resetPassword(String currPassword, String newPassword, String confirmedPassword) {
        if (currPassword.isEmpty()) return ResetPasswordStatus.EMPTY_CURRENT_PASSWORD;
        if (newPassword.isEmpty()) return ResetPasswordStatus.EMPTY_NEW_PASSWORD;
        if (confirmedPassword.isEmpty()) return ResetPasswordStatus.EMPTY_CONFIRM_PASSWORD;

        if (!PasswordUtil.checkPassword(currPassword, SessionManager.currentSession.getCurrentUser().getPasswordHash())) return ResetPasswordStatus.INCORRECT_PASSWORD;
        if (!newPassword.equals(confirmedPassword)) return ResetPasswordStatus.NEW_PASSWORD_MISMATCH;

        SessionManager.currentSession.getCurrentUser().setPassword(PasswordUtil.hashPassword(newPassword));
        resetPassword();
        return ResetPasswordStatus.RESET_PASSWORD_SUCCESS;
    }

    public void resetPassword() {
        repo.resetPasswordByUsername(SessionManager.currentSession.getCurrentUser().getUsername(), SessionManager.currentSession.getCurrentUser().getPasswordHash());
    }

    public void registerUser(User user) {
        repo.addUser(user);
    }
}
