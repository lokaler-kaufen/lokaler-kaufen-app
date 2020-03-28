package de.qaware.mercury.business.login.impl;

import de.qaware.mercury.business.login.KeyProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@EnableConfigurationProperties(KeyProviderConfigurationProperties.class)
public class KeyProviderImpl implements KeyProvider {
    private final String shopJwtSecret;
    private final String adminJwtSecret;
    private final String shopCreationJwtSecret;
    private final String passwordResetJwtSecret;
    private final String reservationCancellationJwtSecret;

    public KeyProviderImpl(KeyProviderConfigurationProperties config) {
        shopJwtSecret = deriveKey(config.getMasterKey(), "shop");
        adminJwtSecret = deriveKey(config.getMasterKey(), "admin");
        shopCreationJwtSecret = deriveKey(config.getMasterKey(), "shop-creation");
        passwordResetJwtSecret = deriveKey(config.getMasterKey(), "password-reset");
        reservationCancellationJwtSecret = deriveKey(config.getMasterKey(), "reservation-cancellation");
    }

    private String deriveKey(String masterKey, String name) {
        // Use HmacSHA256 with the name as secret to derive a new key from the master key
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(name.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new AssertionError("Failed to derive key", e);
        }

        byte[] derivedKey = mac.doFinal(Base64.getDecoder().decode(masterKey));
        return Base64.getEncoder().encodeToString(derivedKey);
    }

    @Override
    public String getShopJwtSecret() {
        return shopJwtSecret;
    }

    @Override
    public String getAdminJwtSecret() {
        return adminJwtSecret;
    }

    @Override
    public String getShopCreationJwtSecret() {
        return shopCreationJwtSecret;
    }

    @Override
    public String getPasswordResetJwtSecret() {
        return passwordResetJwtSecret;
    }

    @Override
    public String getReservationCancellationJwtSecret() {
        return reservationCancellationJwtSecret;
    }
}
