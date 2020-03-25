package de.qaware.mercury.business.email;

import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;

import java.time.LocalDateTime;

public interface EmailService {
    void sendShopCreationLink(String email);

    void sendCustomerReservationConfirmation(Shop shop, String email, String name, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact);

    void sendShopNewReservation(Shop shop, String name, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact);

    void sendShopPasswordResetEmail(String email, PasswordResetToken token);
}
