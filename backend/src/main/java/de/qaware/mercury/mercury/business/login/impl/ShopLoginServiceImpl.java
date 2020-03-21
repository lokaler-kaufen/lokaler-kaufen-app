package de.qaware.mercury.mercury.business.login.impl;

import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.business.shop.Shop;
import org.springframework.stereotype.Service;

@Service
class ShopLoginServiceImpl implements ShopLoginService {

    @Override
    public ShopToken login(String username, String password) {
        return null;
    }

    @Override
    public Shop verify(ShopToken token) {
        return null;
    }
}
