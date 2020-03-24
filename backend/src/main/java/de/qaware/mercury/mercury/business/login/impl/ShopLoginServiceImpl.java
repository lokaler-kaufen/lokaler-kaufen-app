package de.qaware.mercury.mercury.business.login.impl;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.PasswordHasher;
import de.qaware.mercury.mercury.business.login.ShopLogin;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.time.Clock;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.login.ShopLoginRepository;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ShopLoginServiceImpl implements ShopLoginService {
    private final ShopLoginRepository shopLoginRepository;
    private final PasswordHasher passwordHasher;
    private final TokenService tokenService;
    private final UUIDFactory uuidFactory;
    private final ShopRepository shopRepository;
    private final Clock clock;

    @Override
    @Transactional
    public ShopLogin createLogin(Shop shop, String email, String password) {
        String hash = passwordHasher.hash(password);

        ShopLogin shopLogin = new ShopLogin(ShopLogin.Id.random(uuidFactory), shop.getId(), email, hash, clock.nowZoned(), clock.nowZoned());
        shopLoginRepository.insert(shopLogin);
        log.info("Created shop login '{}' for shop '{}'", email, shop.getName());
        return shopLogin;
    }

    @Override
    @Transactional(readOnly = true)
    public ShopToken login(String email, String password) throws LoginException {
        ShopLogin shopLogin = shopLoginRepository.findByEmail(email);
        if (shopLogin == null) {
            log.warn("Shop login '{}' not found", email);
            throw LoginException.forShopLoginEmail(email);
        }

        if (!passwordHasher.verify(password, shopLogin.getPasswordHash())) {
            log.warn("Invalid password for shop login '{}'", email);
            throw LoginException.forShopLoginEmail(email);
        }

        log.info("Logged in shop '{}'", email);
        return tokenService.createShopToken(shopLogin.getId(), shopLogin.getShopId());
    }

    @Override
    @Transactional(readOnly = true)
    public Shop verify(ShopToken token) throws LoginException {
        ShopLogin.Id shopLoginId = tokenService.verifyShopToken(token);

        ShopLogin shopLogin = shopLoginRepository.findById(shopLoginId);
        if (shopLogin == null) {
            log.warn("Token is valid, but shop login with id '{}' not found", shopLoginId);
            throw LoginException.forShopToken(token);
        }

        Shop shop = shopRepository.findById(shopLogin.getShopId());
        if (shop == null) {
            log.warn("Token is valid, shop login found, but shop with id '{}' not found", shopLogin.getShopId());
            throw LoginException.forShopToken(token);
        }

        return shop;
    }
}
