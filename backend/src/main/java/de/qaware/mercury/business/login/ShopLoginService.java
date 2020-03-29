package de.qaware.mercury.business.login;

import de.qaware.mercury.business.shop.Shop;

public interface ShopLoginService {
    ShopLogin createLogin(Shop shop, String email, String password);

    TokenWithExpiry<ShopToken> login(String email, String password) throws LoginException;

    Shop verify(ShopToken token) throws LoginException;

    void sendPasswordResetLink(String email);

    void resetPassword(String email, String newPassword) throws ShopLoginNotFoundException;

    boolean hasLogin(String email);
}
