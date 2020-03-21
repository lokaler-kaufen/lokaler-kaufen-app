package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.BusinessException;
import de.qaware.mercury.mercury.business.admin.Admin;

public class LoginException extends BusinessException {
    private LoginException(String message) {
        super(message);
    }

    public static LoginException forAdminEmail(String email) {
        return new LoginException(String.format("Login failed for admin '%s'", email));
    }

    public static LoginException forAdminId(Admin.Id adminId) {
        return new LoginException(String.format("Login failed for admin id '%s'", adminId));
    }

    public static LoginException forAdminToken(AdminToken token) {
        return new LoginException(String.format("Login failed for admin with token '%s'", token.getToken()));
    }
}
