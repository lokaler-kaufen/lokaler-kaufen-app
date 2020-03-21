package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.shop.Shop;

public interface TokenService {
    AdminToken createAdminToken(Admin.Id adminId);

    Admin.Id verifyAdminToken(AdminToken token) throws LoginException;

    ShopToken createShopToken(ShopLogin.Id shopLoginId, Shop.Id shopId);

    ShopLogin.Id verifyShopToken(ShopToken token) throws LoginException;

    ShopCreationToken createShopCreationToken(String email);

    String verifyShopCreationToken(ShopCreationToken token) throws LoginException;
}
