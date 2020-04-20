package de.qaware.mercury.business.login.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.login.AdminToken;
import de.qaware.mercury.business.login.KeyProvider;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.login.ReservationCancellationToken;
import de.qaware.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.login.ShopToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.login.TokenTechnicalException;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellation;
import de.qaware.mercury.business.reservation.ReservationCancellationSide;
import de.qaware.mercury.business.shop.InvalidShopIdException;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TokenServiceImpl implements TokenService {
    private static final String SHOP_ISSUER = "mercury-shop";
    private static final String ADMIN_ISSUER = "mercury-admin";
    private static final String SHOP_CREATION_ISSUER = "mercury-shop-creation";
    private static final String PASSWORD_RESET_ISSUER = "mercury-password-reset";
    private static final String RESERVATION_CANCELLATION_ISSUER = "mercury-reservation-cancellation";

    private static final Duration ADMIN_TOKEN_EXPIRY = Duration.ofDays(1);
    private static final Duration SHOP_TOKEN_EXPIRY = Duration.ofDays(1);
    private static final Duration SHOP_CREATION_TOKEN_EXPIRY = Duration.ofDays(7);
    private static final Duration PASSWORD_RESET_TOKEN_EXPIRY = Duration.ofDays(2);

    private final KeyProvider keyProvider;
    private final Clock clock;

    @Override
    public VerifiedToken<Admin.Id, AdminToken> createAdminToken(Admin.Id adminId) {
        try {
            Algorithm algorithm = getAlgorithm(keyProvider.getAdminJwtSecret());
            ZonedDateTime expiry = clock.nowZoned().plus(ADMIN_TOKEN_EXPIRY);

            String token = JWT.create()
                .withIssuedAt(clock.nowAsLegacyDate())
                .withNotBefore(clock.nowAsLegacyDate())
                .withExpiresAt(Date.from(expiry.toInstant()))
                .withIssuer(ADMIN_ISSUER)
                .withSubject(adminId.toString())
                .sign(algorithm);

            return new VerifiedToken<>(adminId, AdminToken.of(token), expiry.toInstant());
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException("Failed to create admin token for " + adminId, exception);
        }
    }

    @Override
    public VerifiedToken<Admin.Id, AdminToken> verifyAdminToken(AdminToken token) throws LoginException {
        try {
            JWTVerifier verifier = createJwtVerifier(keyProvider.getAdminJwtSecret(), ADMIN_ISSUER);
            DecodedJWT jwt = verifier.verify(token.getToken());

            Admin.Id adminId = Admin.Id.parse(jwt.getSubject());
            log.debug("Verified token for admin {}", adminId);

            return new VerifiedToken<>(adminId, token, jwt.getExpiresAt().toInstant());
        } catch (JWTVerificationException e) {
            log.warn("Admin token verification failed for token '{}'", token, e);
            throw LoginException.forAdminToken(token);
        }
    }

    @Override
    public VerifiedToken<ShopLogin.Id, ShopToken> createShopToken(ShopLogin.Id shopLoginId, Shop.Id shopId) {
        try {
            Algorithm algorithm = getAlgorithm(keyProvider.getShopJwtSecret());
            ZonedDateTime expiry = clock.nowZoned().plus(SHOP_TOKEN_EXPIRY);

            String token = JWT.create()
                .withIssuedAt(clock.nowAsLegacyDate())
                .withNotBefore(clock.nowAsLegacyDate())
                .withExpiresAt(Date.from(expiry.toInstant()))
                .withIssuer(SHOP_ISSUER)
                .withSubject(shopLoginId.toString())
                .withClaim("shop", shopId.toString())
                .sign(algorithm);

            return new VerifiedToken<>(shopLoginId, ShopToken.of(token), expiry.toInstant());
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to create shop token for login %s, shop %s", shopLoginId, shopId), exception);
        }
    }

    @Override
    public VerifiedToken<ShopLogin.Id, ShopToken> verifyShopToken(ShopToken token) throws LoginException {
        try {
            JWTVerifier verifier = createJwtVerifier(keyProvider.getShopJwtSecret(), SHOP_ISSUER);
            DecodedJWT jwt = verifier.verify(token.getToken());

            ShopLogin.Id shopLoginId = ShopLogin.Id.parse(jwt.getSubject());
            Shop.Id shopId = Shop.Id.parse(jwt.getClaim("shop").asString());

            log.debug("Verified token for shop login {}, shop {}", shopLoginId, shopId);
            return new VerifiedToken<>(shopLoginId, token, jwt.getExpiresAt().toInstant());
        } catch (JWTVerificationException e) {
            log.warn("Shop token verification failed for token '{}'", token, e);
            throw LoginException.forShopToken(token);
        } catch (InvalidShopIdException e) {
            throw new AssertionError("Invalid shop id in token", e);
        }
    }

    @Override
    public ShopCreationToken createShopCreationToken(String email) {
        try {
            Algorithm algorithm = getAlgorithm(keyProvider.getShopCreationJwtSecret());
            ZonedDateTime expiry = clock.nowZoned().plus(SHOP_CREATION_TOKEN_EXPIRY);

            String token = JWT.create()
                .withIssuedAt(clock.nowAsLegacyDate())
                .withNotBefore(clock.nowAsLegacyDate())
                .withExpiresAt(Date.from(expiry.toInstant()))
                .withIssuer(SHOP_CREATION_ISSUER)
                .withSubject(email)
                .sign(algorithm);

            return ShopCreationToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to create shop creation token for email '%s'", email), exception);
        }
    }

    @Override
    public String verifyShopCreationToken(ShopCreationToken token) throws LoginException {
        try {
            JWTVerifier verifier = createJwtVerifier(keyProvider.getShopCreationJwtSecret(), SHOP_CREATION_ISSUER);
            DecodedJWT jwt = verifier.verify(token.getToken());
            String email = jwt.getSubject();

            log.debug("Verified token for shop creation, email '{}'", email);
            return email;
        } catch (JWTVerificationException e) {
            log.warn("Shop creation token verification failed for token '{}'", token, e);
            throw LoginException.forShopCreationToken(token);
        }
    }

    @Override
    public PasswordResetToken createPasswordResetToken(String email) {
        try {
            Algorithm algorithm = getAlgorithm(keyProvider.getPasswordResetJwtSecret());
            ZonedDateTime expiry = clock.nowZoned().plus(PASSWORD_RESET_TOKEN_EXPIRY);

            String token = JWT.create()
                .withIssuedAt(clock.nowAsLegacyDate())
                .withNotBefore(clock.nowAsLegacyDate())
                .withExpiresAt(Date.from(expiry.toInstant()))
                .withIssuer(PASSWORD_RESET_ISSUER)
                .withSubject(email)
                .sign(algorithm);

            return PasswordResetToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to create password reset token for email '%s'", email), exception);
        }
    }

    @Override
    public String verifyPasswordResetToken(PasswordResetToken token) throws LoginException {
        try {
            JWTVerifier verifier = createJwtVerifier(keyProvider.getPasswordResetJwtSecret(), PASSWORD_RESET_ISSUER);
            DecodedJWT jwt = verifier.verify(token.getToken());
            String email = jwt.getSubject();

            log.debug("Verified token for password reset, email '{}'", email);
            return email;
        } catch (JWTVerificationException e) {
            log.warn("Password reset token verification failed for token '{}'", token, e);
            throw LoginException.forPasswordResetToken(token);
        }
    }

    @Override
    public ReservationCancellationToken createReservationCancellationToken(Reservation.Id reservationId, ReservationCancellationSide side, LocalDateTime slotStart) {
        try {
            Algorithm algorithm = getAlgorithm(keyProvider.getReservationCancellationJwtSecret());
            String token = JWT.create()
                .withIssuedAt(clock.nowAsLegacyDate())
                .withNotBefore(clock.nowAsLegacyDate())
                // The token expires in the moment the reserved slot starts
                .withExpiresAt(Date.from(slotStart.toInstant(ZoneId.systemDefault().getRules().getOffset(slotStart))))
                .withIssuer(RESERVATION_CANCELLATION_ISSUER)
                .withSubject(reservationId.getId().toString())
                .withClaim("side", side.getId())
                .sign(algorithm);

            return ReservationCancellationToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException(String.format("Failed to create reservation cancellation token for reservation '%s', side %s", reservationId, side), exception);
        }
    }

    @Override
    public ReservationCancellation verifyReservationCancellationToken(ReservationCancellationToken token) throws LoginException {
        try {
            JWTVerifier verifier = createJwtVerifier(keyProvider.getReservationCancellationJwtSecret(), RESERVATION_CANCELLATION_ISSUER);
            DecodedJWT jwt = verifier.verify(token.getToken());

            Reservation.Id reservationId = Reservation.Id.parse(jwt.getSubject());
            ReservationCancellationSide side = ReservationCancellationSide.parse(jwt.getClaim("side").asString());

            log.debug("Verified token for reservation cancellation, reservation id '{}', side {}", reservationId, side);
            return new ReservationCancellation(reservationId, side);
        } catch (JWTVerificationException e) {
            log.warn("Reservation cancellation token verification failed for token '{}'", token, e);
            throw LoginException.forReservationCancellationToken(token);
        }
    }

    private JWTVerifier createJwtVerifier(String secret, String issuer) {
        Verification verification = JWT.require(getAlgorithm(secret))
            .withIssuer(issuer);

        // We have to set the clock to allowed mocked time in tests
        // There's no build(Clock) method on Verification, so we need to cast it.
        // Ugly, but I don't see any other way.
        JWTVerifier.BaseVerification baseVerification = (JWTVerifier.BaseVerification) verification;
        return baseVerification.build(clock::nowAsLegacyDate);
    }

    private Algorithm getAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }
}
