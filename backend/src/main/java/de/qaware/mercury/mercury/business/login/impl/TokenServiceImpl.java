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
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.login.TokenTechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class TokenServiceImpl implements TokenService {
    // TODO MKA: Move to config!
    private static final String SHOP_JWT_SECRET = "shop-secret";
    private static final String ADMIN_JWT_SECRET = "admin-secret";
    private static final String ADMIN_ISSUER = "mercury-admin";

    @Override
    public AdminToken createAdminToken(Admin.Id adminId) {
        try {
            Algorithm algorithm = getAlgorithm(ADMIN_JWT_SECRET);
            String token = JWT.create()
                .withIssuer(ADMIN_ISSUER)
                .withSubject(adminId.toString())
                .sign(algorithm);
            // TODO MKA: Add expiry!

            return AdminToken.of(token);
        } catch (JWTCreationException exception) {
            throw new TokenTechnicalException("Failed to created admin token for admin " + adminId, exception);
        }
    }

    @Override
    public Admin.Id verifyAdminToken(AdminToken token) throws LoginException {
        try {
            Algorithm algorithm = getAlgorithm(ADMIN_JWT_SECRET);
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

    private Algorithm getAlgorithm(String jwtSecret) {
        return Algorithm.HMAC256(jwtSecret);
    }
}
