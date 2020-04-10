package de.qaware.mercury.business.login.impl;

import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.PasswordHasher;
import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.login.ShopLoginNotFoundException;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.login.ShopToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.login.ShopLoginRepository;
import de.qaware.mercury.storage.shop.ShopRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
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
    private final EmailService emailService;

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
    public VerifiedToken<ShopLogin.Id, ShopToken> login(String email, String password) throws LoginException {
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
        VerifiedToken<ShopLogin.Id, ShopToken> verifiedToken = tokenService.verifyShopToken(token);

        ShopLogin shopLogin = shopLoginRepository.findById(verifiedToken.getId());
        if (shopLogin == null) {
            log.warn("Token is valid, but shop login with id '{}' not found", verifiedToken);
            throw LoginException.forShopToken(token);
        }

        Shop shop = shopRepository.findById(shopLogin.getShopId());
        if (shop == null) {
            log.warn("Token is valid, shop login found, but shop with id '{}' not found", shopLogin.getShopId());
            throw LoginException.forShopToken(token);
        }

        return shop;
    }

    @Override
    @Transactional(readOnly = true)
    public void sendPasswordResetLink(String email) {
        ShopLogin shopLogin = shopLoginRepository.findByEmail(email);
        if (shopLogin == null) {
            log.warn("Password reset request for non-existing login '{}'", email);
            return;
        }

        PasswordResetToken token = tokenService.createPasswordResetToken(email);
        log.info("Sending shop passwort reset email to '{}'", email);
        emailService.sendShopPasswordResetEmail(email, token);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String newPassword) throws ShopLoginNotFoundException {
        ShopLogin login = shopLoginRepository.findByEmail(email);
        if (login == null) {
            throw new ShopLoginNotFoundException(email);
        }

        log.info("Resetting password for shop login '{}'", email);
        String newPasswordHash = passwordHasher.hash(newPassword);
        shopLoginRepository.update(login.withPasswordHash(newPasswordHash));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasLogin(String email) {
        return shopLoginRepository.findByEmail(email) != null;
    }

    @Override
    @Nullable
    public VerifiedToken<ShopLogin.Id, ShopToken> getTokenInfo(ShopToken token) {
        try {
            return tokenService.verifyShopToken(token);
        } catch (LoginException e) {
            log.debug("Failed to verify shop token for getTokenInfo()", e);
            return null;
        }
    }
}
