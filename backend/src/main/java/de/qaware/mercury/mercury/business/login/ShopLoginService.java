package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.shop.Shop;

public interface ShopLoginService {
    ShopToken login(String username, String password);

    Shop verify(ShopToken token);
}
