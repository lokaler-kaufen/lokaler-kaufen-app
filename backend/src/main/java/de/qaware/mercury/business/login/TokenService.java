package de.qaware.mercury.business.login;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellation;
import de.qaware.mercury.business.reservation.ReservationCancellationSide;
import de.qaware.mercury.business.shop.Shop;

import java.time.LocalDateTime;

public interface TokenService {
    TokenWithExpiry<AdminToken> createAdminToken(Admin.Id adminId);

    VerifiedToken<Admin.Id> verifyAdminToken(AdminToken token) throws LoginException;

    TokenWithExpiry<ShopToken> createShopToken(ShopLogin.Id shopLoginId, Shop.Id shopId);

    VerifiedToken<ShopLogin.Id> verifyShopToken(ShopToken token) throws LoginException;

    ShopCreationToken createShopCreationToken(String email);

    String verifyShopCreationToken(ShopCreationToken token) throws LoginException;

    PasswordResetToken createPasswordResetToken(String email);

    String verifyPasswordResetToken(PasswordResetToken token) throws LoginException;

    ReservationCancellationToken createReservationCancellationToken(Reservation.Id reservationId, ReservationCancellationSide side, LocalDateTime slotStart);

    ReservationCancellation verifyReservationCancellationToken(ReservationCancellationToken token) throws LoginException;
}
