package de.qaware.mercury.mercury.business.login.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.login.AdminToken;
import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.mercury.business.login.ShopLogin;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.login.TokenTechnicalException;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableConfigurationProperties(TokenServiceConfigurationProperties.class)
class TokenServiceImpl implements TokenService {
    private static final String SHOP_ISSUER = "mercury-shop";
    private static final String ADMIN_ISSUER = "mercury-admin";
    private static final String SHOP_CREATION_ISSUER = "mercury-shop-creation";

    private final TokenServiceConfigurationProperties config;

    TokenServiceImpl(TokenServiceConfigurationProperties config) {
        this.config = config;
    }

    @Override
    public AdminToken createAdminToken(Admin.Id adminId) {
        try {
            Algorithm algorithm = getAlgorithm(config.getAdminJwtSecret());
            String token = JWT.create()
                .withIssuer(ADMIN_ISSUER)
                .withSubject(adminId.toString())
                .sign(algorithm);
            // TODO MKA: Add expiry!

            return AdminToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException("Failed to created admin token for " + adminId, exception);
        }
    }

    @Override
    public Admin.Id verifyAdminToken(AdminToken token) throws LoginException {
        try {
            Algorithm algorithm = getAlgorithm(config.getAdminJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ADMIN_ISSUER)
                .build();
            DecodedJWT jwt = verifier.verify(token.getToken());

            Admin.Id adminId = Admin.Id.parse(jwt.getSubject());
            log.debug("Verified token for admin {}", adminId);
            return adminId;
        } catch (JWTVerificationException e) {
            log.warn("Admin token verification failed for token '{}'", token, e);
            throw LoginException.forAdminToken(token);
        }
    }

    @Override
    public ShopToken createShopToken(ShopLogin.Id shopLoginId, Shop.Id shopId) {
        try {
            Algorithm algorithm = getAlgorithm(config.getShopJwtSecret());
            String token = JWT.create()
                .withIssuer(SHOP_ISSUER)
                .withSubject(shopLoginId.toString())
                .withClaim("shop", shopId.toString())
                .sign(algorithm);
            // TODO MKA: Add expiry!

            return ShopToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to created shop token for login %s, shop %s", shopLoginId, shopId), exception);
        }
    }

    @Override
    public ShopLogin.Id verifyShopToken(ShopToken token) throws LoginException {
        try {
            Algorithm algorithm = getAlgorithm(config.getShopJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(SHOP_ISSUER)
                .build();
            DecodedJWT jwt = verifier.verify(token.getToken());

            ShopLogin.Id shopLoginId = ShopLogin.Id.parse(jwt.getSubject());
            Shop.Id shopId = Shop.Id.parse(jwt.getClaim("shop").asString());

            log.debug("Verified token for shop login {}, shop {}", shopLoginId, shopId);
            return shopLoginId;
        } catch (JWTVerificationException e) {
            log.warn("Shop token verification failed for token '{}'", token, e);
            throw LoginException.forShopToken(token);
        }
    }

    @Override
    public ShopCreationToken createShopCreationToken(String email) {
        try {
            Algorithm algorithm = getAlgorithm(config.getShopCreationJwtSecret());
            String token = JWT.create()
                .withIssuer(SHOP_CREATION_ISSUER)
                .withSubject(email)
                .sign(algorithm);
            // TODO MKA: Add expiry!

            return ShopCreationToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to created shop creation token for email '%s'", email), exception);
        }
    }

    @Override
    public String verifyShopCreationToken(ShopCreationToken token) throws LoginException {
        try {
            Algorithm algorithm = getAlgorithm(config.getShopCreationJwtSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(SHOP_CREATION_ISSUER)
                .build();
            DecodedJWT jwt = verifier.verify(token.getToken());
            String email = jwt.getSubject();

            log.debug("Verified token for shop creation, email '{}'", email);
            return email;
        } catch (JWTVerificationException e) {
            log.warn("Shop creation token verification failed for token '{}'", token, e);
            throw LoginException.forShopCreationToken(token);
        }
    }

    private Algorithm getAlgorithm(String jwtSecret) {
        return Algorithm.HMAC256(jwtSecret);
    }
}
