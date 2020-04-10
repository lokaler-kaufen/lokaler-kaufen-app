package de.qaware.mercury.business.login;

import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

public interface ShopLoginService {
    ShopLogin createLogin(Shop shop, String email, String password);

    VerifiedToken<ShopLogin.Id, ShopToken> login(String email, String password) throws LoginException;

    Shop verify(ShopToken token) throws LoginException;

    void sendPasswordResetLink(String email);

    void resetPassword(String email, String newPassword) throws ShopLoginNotFoundException;

    boolean hasLogin(String email);

    @Nullable
    VerifiedToken<ShopLogin.Id, ShopToken> getTokenInfo(ShopToken token);
}
