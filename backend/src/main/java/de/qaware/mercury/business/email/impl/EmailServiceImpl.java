package de.qaware.mercury.business.email.impl;

import de.qaware.mercury.business.email.EmailSender;
import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.email.SendEmailException;
import de.qaware.mercury.business.i18n.DateTimeI18nService;
import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.login.ReservationCancellationToken;
import de.qaware.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.reservation.ReservationCancellationSide;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
@EnableConfigurationProperties(EmailConfigurationProperties.class)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EmailServiceImpl implements EmailService {
    static final String SHOP_CREATION_SUBJECT = "Dein Laden auf lokaler.kaufen";
    private static final String CUSTOMER_RESERVATION_CONFIRMATION_SUBJECT = "Reservierungsbestätigung auf lokaler.kaufen";
    private static final String SHOP_NEW_RESERVATION_SUBJECT = "Es gibt eine neue Reservierung auf lokaler.kaufen";
    private static final String SHOP_RESET_PASSWORD_SUBJECT = "Passwort auf lokaler.kaufen zurücksetzen";
    private static final String RESERVATION_CANCELLATION_SUBJECT = "Dein Termin auf lokaler.kaufen wurde leider abgesagt";
    private static final String RESERVATION_CANCELLATION_CONFIRMATION_SUBJECT = "Absagebestätigung von lokaler.kaufen";
    private static final String SHOP_CREATED_APPROVAL_NEEDED_SUBJECT = "Gleich kann's auf lokaler.kaufen losgehen!";
    private static final String SHOP_APPROVED_SUBJECT = "Es kann auf lokaler.kaufen losgehen!";
    private static final String SHOP_DISAPPROVED_SUBJECT = "Dein Laden auf lokaler.kaufen wurde deaktiviert";

    private final EmailSender emailSender;
    private final EmailConfigurationProperties config;
    private final TokenService tokenService;
    private final DateTimeI18nService dateTimeI18nService;

    @Override
    public void sendShopCreationLink(String email) {
        ShopCreationToken token = tokenService.createShopCreationToken(email);

        String creationLink = config.getCreationLinkTemplate()
            .replace("{{ token }}", token.getToken());
        String body = loadTemplate("/email/shop-creation.txt")
            .replace("{{ link }}", creationLink);

        emailSender.sendEmail(email, SHOP_CREATION_SUBJECT, body);
    }

    @Override
    public void sendCustomerReservationConfirmation(Shop shop, String email, String name, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact, Reservation.Id reservationId) {
        ReservationCancellationToken token = tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.CUSTOMER, slotStart);
        String cancelReservationLink = config.getReservationCancellationLinkTemplate()
            .replace("{{ token }}", token.getToken());

        String body = loadTemplate("/email/customer-reservation-confirmation.txt")
            .replace("{{ name }}", name)
            .replace("{{ shopName }}", shop.getName())
            .replace("{{ ownerName }}", shop.getOwnerName())
            .replace("{{ contactType }}", contactType.getHumanReadable())
            .replace("{{ date }}", dateTimeI18nService.formatDate(slotStart))
            .replace("{{ start }}", dateTimeI18nService.formatTime(slotStart))
            .replace("{{ end }}", dateTimeI18nService.formatTime(slotEnd))
            .replace("{{ contact }}", contact)
            .replace("{{ cancelReservationLink }}", cancelReservationLink);

        emailSender.sendEmail(email, CUSTOMER_RESERVATION_CONFIRMATION_SUBJECT, body);
    }

    @Override
    public void sendShopNewReservation(Shop shop, String name, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact, Reservation.Id reservationId) {
        ReservationCancellationToken token = tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.SHOP, slotStart);
        String cancelReservationLink = config.getReservationCancellationLinkTemplate()
            .replace("{{ token }}", token.getToken());

        String body = loadTemplate("/email/shop-new-reservation.txt")
            .replace("{{ name }}", name)
            .replace("{{ ownerName }}", shop.getOwnerName())
            .replace("{{ contactType }}", contactType.getHumanReadable())
            .replace("{{ date }}", dateTimeI18nService.formatDate(slotStart))
            .replace("{{ start }}", dateTimeI18nService.formatTime(slotStart))
            .replace("{{ end }}", dateTimeI18nService.formatTime(slotEnd))
            .replace("{{ contact }}", contact)
            .replace("{{ cancelReservationLink }}", cancelReservationLink);

        emailSender.sendEmail(shop.getEmail(), SHOP_NEW_RESERVATION_SUBJECT, body);
    }

    @Override
    public void sendShopPasswordResetEmail(String email, PasswordResetToken token) {
        String resetLink = config.getShopPasswordResetLinkTemplate()
            .replace("{{ token }}", token.getToken());

        String body = loadTemplate("/email/shop-passwort-reset.txt")
            .replace("{{ link }}", resetLink);

        emailSender.sendEmail(email, SHOP_RESET_PASSWORD_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationToCustomer(Shop shop, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-to-customer.txt")
            .replace("{{ shopName }}", shop.getName())
            .replace("{{ customerName }}", reservation.getName())
            .replace("{{ date }}", dateTimeI18nService.formatDate(reservation.getStart()))
            .replace("{{ start }}", dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(reservation.getEmail(), RESERVATION_CANCELLATION_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationToShop(Shop shop, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-to-shop.txt")
            .replace("{{ shopName }}", shop.getName())
            .replace("{{ customerName }}", reservation.getName())
            .replace("{{ date }}", dateTimeI18nService.formatDate(reservation.getStart()))
            .replace("{{ start }}", dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(shop.getEmail(), RESERVATION_CANCELLATION_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationConfirmation(String email, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-confirmation.txt")
            .replace("{{ date }}", dateTimeI18nService.formatDate(reservation.getStart()))
            .replace("{{ start }}", dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(email, RESERVATION_CANCELLATION_CONFIRMATION_SUBJECT, body);
    }

    @Override
    public void sendShopCreatedApprovalNeeded(Shop shop) {
        String body = loadTemplate("/email/shop-created-approval-needed.txt")
            .replace("{{ ownerName }}", shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_CREATED_APPROVAL_NEEDED_SUBJECT, body);
    }

    @Override
    public void sendShopApproved(Shop shop) {
        String body = loadTemplate("/email/shop-approved.txt")
            .replace("{{ ownerName }}", shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_APPROVED_SUBJECT, body);
    }

    @Override
    public void sendShopApprovalRevoked(Shop shop) {
        String body = loadTemplate("/email/shop-disapproved.txt")
            .replace("{{ ownerName }}", shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_DISAPPROVED_SUBJECT, body);
    }

    private String loadTemplate(String location) {
        try (InputStream stream = EmailServiceImpl.class.getResourceAsStream(location)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SendEmailException(String.format("Failed to read resource '%s'", location), e);
        }
    }
}
