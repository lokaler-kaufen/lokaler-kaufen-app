package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

public interface ShopLoginService {
    ShopLogin createLogin(Shop shop, String email, String password);

    ShopToken login(String email, String password) throws LoginException;

    Shop verify(ShopToken token) throws LoginException;

    @Nullable
    ShopLogin findByEmail(String email);
}
