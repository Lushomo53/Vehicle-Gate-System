package org.example.controller;

import org.example.controller.service.SessionManager;
import org.example.util.OTP;

public class VerificationController {
    public VerificationStatus verify(String codeEntered) {
        if (codeEntered.isEmpty()) return VerificationStatus.EMPTY_FIELD;

        OTP otp = SessionManager.currentSession.getOtp();

        if (!otp.isValid()) return VerificationStatus.INVALID_OTP;
        if (codeEntered.equals(otp.toString())) {
            otp.markUsed();
            new UserController().registerUser(SessionManager.currentSession.getCurrentUser());
            return VerificationStatus.VERIFIED;
        }
        return VerificationStatus.INVALID_OTP;
    }
}
