package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.BusinessException;

public class LoginException extends BusinessException {
    private LoginException(String message) {
        super(message);
    }

    public static LoginException forAdminEmail(String email) {
        return new LoginException(String.format("Login failed for admin '%s'", email));
    }

    public static LoginException forShopLoginEmail(String email) {
        return new LoginException(String.format("Login failed for shop '%s'", email));
    }

    public static LoginException forAdminToken(AdminToken token) {
        return new LoginException(String.format("Login failed for admin with token '%s'", token.getToken()));
    }

    public static LoginException forShopToken(ShopToken token) {
        return new LoginException(String.format("Login failed for shop with token '%s'", token.getToken()));
    }
}
