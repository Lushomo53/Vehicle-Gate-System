package org.gatesystem.controller;

import org.gatesystem.controller.service.SessionManager;
import org.gatesystem.util.OTP;

public class VerificationController {
    public VerificationStatus verify(String codeEntered, VerificationPurpose purpose) {
        if (codeEntered.isEmpty()) return VerificationStatus.EMPTY_FIELD;

        OTP otp = SessionManager.currentSession.getOtp();

        if (!otp.isValid()) return VerificationStatus.INVALID_OTP;
        if (codeEntered.equals(otp.toString())) {
            otp.markUsed();
            UserController controller = new UserController();
            if (purpose == VerificationPurpose.REGISTER) {
                controller.registerUser(SessionManager.currentSession.getCurrentUser());
            }
            return VerificationStatus.VERIFIED;
        }
        return VerificationStatus.INVALID_OTP;
    }
}
