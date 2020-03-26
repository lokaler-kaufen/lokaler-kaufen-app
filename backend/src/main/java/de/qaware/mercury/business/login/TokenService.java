package de.qaware.mercury.business.login;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellation;
import de.qaware.mercury.business.reservation.ReservationCancellationSide;
import de.qaware.mercury.business.shop.Shop;

public interface TokenService {
    AdminToken createAdminToken(Admin.Id adminId);

    Admin.Id verifyAdminToken(AdminToken token) throws LoginException;

    ShopToken createShopToken(ShopLogin.Id shopLoginId, Shop.Id shopId);

    ShopLogin.Id verifyShopToken(ShopToken token) throws LoginException;

    ShopCreationToken createShopCreationToken(String email);

    String verifyShopCreationToken(ShopCreationToken token) throws LoginException;

    PasswordResetToken createPasswordResetToken(String email);

    String verifyPasswordResetToken(PasswordResetToken token) throws LoginException;

    ReservationCancellationToken createReservationCancellationToken(Reservation.Id reservationId, ReservationCancellationSide side);

    ReservationCancellation verifyReservationCancellationToken(ReservationCancellationToken token) throws LoginException;
}
