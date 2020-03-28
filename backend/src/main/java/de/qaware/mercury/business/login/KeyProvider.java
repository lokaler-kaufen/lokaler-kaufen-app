package de.qaware.mercury.business.login;

public interface KeyProvider {
    String getShopJwtSecret();

    String getAdminJwtSecret();

    String getShopCreationJwtSecret();

    String getPasswordResetJwtSecret();

    String getReservationCancellationJwtSecret();
}
