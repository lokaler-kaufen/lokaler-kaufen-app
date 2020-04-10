package de.qaware.mercury.business.email.impl;

import de.qaware.mercury.business.admin.Admin;
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
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@EnableConfigurationProperties(EmailConfigurationProperties.class)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class EmailServiceImpl implements EmailService {
    static final String SHOP_CREATION_SUBJECT = "Ihr Laden auf lokaler.kaufen";
    private static final String CUSTOMER_RESERVATION_CONFIRMATION_SUBJECT = "Reservierungsbestätigung auf lokaler.kaufen";
    private static final String SHOP_NEW_RESERVATION_SUBJECT = "Es gibt eine neue Reservierung auf lokaler.kaufen";
    private static final String SHOP_RESET_PASSWORD_SUBJECT = "Passwort auf lokaler.kaufen zurücksetzen";
    private static final String RESERVATION_CANCELLATION_SUBJECT = "Ihr Termin auf lokaler.kaufen wurde leider abgesagt";
    private static final String RESERVATION_CANCELLATION_CONFIRMATION_SUBJECT = "Absagebestätigung von lokaler.kaufen";
    private static final String SHOP_CREATED_APPROVAL_NEEDED_SUBJECT = "Gleich kann's auf lokaler.kaufen losgehen!";
    private static final String SHOP_APPROVED_SUBJECT = "Es kann auf lokaler.kaufen losgehen!";
    private static final String SHOP_DISAPPROVED_SUBJECT = "Ihr Laden auf lokaler.kaufen wurde deaktiviert";
    private static final String ADMIN_SHOP_APPROVAL_NEEDED = "Ein Laden auf lokaler.kaufen wartet auf Freischaltung";

    private final EmailSender emailSender;
    private final EmailConfigurationProperties config;
    private final TokenService tokenService;
    private final DateTimeI18nService dateTimeI18nService;

    @Override
    public void sendShopCreationLink(String email) {
        ShopCreationToken token = tokenService.createShopCreationToken(email);

        String creationLink = config.getCreationLinkTemplate()
            .replace(EmailTemplateConstants.TOKEN, token.getToken());
        String body = loadTemplate("/email/shop-creation.txt")
            .replace(EmailTemplateConstants.SHOP_CREATION_LINK, creationLink);

        emailSender.sendEmail(email, SHOP_CREATION_SUBJECT, body);
    }

    @Override
    public void sendCustomerReservationConfirmation(Shop shop, String email, String customerName, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact, Reservation.Id reservationId) {
        ReservationCancellationToken token = tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.CUSTOMER, slotStart);
        String cancelReservationLink = config.getReservationCancellationLinkTemplate()
            .replace(EmailTemplateConstants.TOKEN, token.getToken());

        String body = loadTemplate("/email/customer-reservation-confirmation.txt")
            .replace(EmailTemplateConstants.CUSTOMER_NAME, customerName)
            .replace(EmailTemplateConstants.SHOP_NAME, shop.getName())
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName())
            .replace(EmailTemplateConstants.CONTACT_TYPE, contactType.getHumanReadable())
            .replace(EmailTemplateConstants.SLOT_DATE, dateTimeI18nService.formatDate(slotStart))
            .replace(EmailTemplateConstants.SLOT_START_TIME, dateTimeI18nService.formatTime(slotStart))
            .replace(EmailTemplateConstants.SLOT_END_TIME, dateTimeI18nService.formatTime(slotEnd))
            .replace(EmailTemplateConstants.CONTACT, contact)
            .replace(EmailTemplateConstants.CANCEL_RESERVATION_LINK, cancelReservationLink);

        emailSender.sendEmail(email, CUSTOMER_RESERVATION_CONFIRMATION_SUBJECT, body);
    }

    @Override
    public void sendShopNewReservation(Shop shop, String customerName, LocalDateTime slotStart, LocalDateTime slotEnd, ContactType contactType, String contact, Reservation.Id reservationId) {
        ReservationCancellationToken token = tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.SHOP, slotStart);
        String cancelReservationLink = config.getReservationCancellationLinkTemplate()
            .replace(EmailTemplateConstants.TOKEN, token.getToken());

        String body = loadTemplate("/email/shop-new-reservation.txt")
            .replace(EmailTemplateConstants.CUSTOMER_NAME, customerName)
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName())
            .replace(EmailTemplateConstants.CONTACT_TYPE, contactType.getHumanReadable())
            .replace(EmailTemplateConstants.SLOT_DATE, dateTimeI18nService.formatDate(slotStart))
            .replace(EmailTemplateConstants.SLOT_START_TIME, dateTimeI18nService.formatTime(slotStart))
            .replace(EmailTemplateConstants.SLOT_END_TIME, dateTimeI18nService.formatTime(slotEnd))
            .replace(EmailTemplateConstants.CONTACT, contact)
            .replace(EmailTemplateConstants.CANCEL_RESERVATION_LINK, cancelReservationLink);

        emailSender.sendEmail(shop.getEmail(), SHOP_NEW_RESERVATION_SUBJECT, body);
    }

    @Override
    public void sendShopPasswordResetEmail(String email, PasswordResetToken token) {
        String resetLink = config.getShopPasswordResetLinkTemplate()
            .replace(EmailTemplateConstants.TOKEN, token.getToken());

        String body = loadTemplate("/email/shop-passwort-reset.txt")
            .replace(EmailTemplateConstants.PASSWORD_RESET_LINK, resetLink);

        emailSender.sendEmail(email, SHOP_RESET_PASSWORD_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationToCustomer(Shop shop, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-to-customer.txt")
            .replace(EmailTemplateConstants.SHOP_NAME, shop.getName())
            .replace(EmailTemplateConstants.CUSTOMER_NAME, reservation.getName())
            .replace(EmailTemplateConstants.SLOT_DATE, dateTimeI18nService.formatDate(reservation.getStart()))
            .replace(EmailTemplateConstants.SLOT_START_TIME, dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(reservation.getEmail(), RESERVATION_CANCELLATION_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationToShop(Shop shop, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-to-shop.txt")
            .replace(EmailTemplateConstants.SHOP_NAME, shop.getName())
            .replace(EmailTemplateConstants.CUSTOMER_NAME, reservation.getName())
            .replace(EmailTemplateConstants.SLOT_DATE, dateTimeI18nService.formatDate(reservation.getStart()))
            .replace(EmailTemplateConstants.SLOT_START_TIME, dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(shop.getEmail(), RESERVATION_CANCELLATION_SUBJECT, body);
    }

    @Override
    public void sendReservationCancellationConfirmation(String email, Reservation reservation) {
        String body = loadTemplate("/email/reservation-cancellation-confirmation.txt")
            .replace(EmailTemplateConstants.SLOT_DATE, dateTimeI18nService.formatDate(reservation.getStart()))
            .replace(EmailTemplateConstants.SLOT_START_TIME, dateTimeI18nService.formatTime(reservation.getStart()));

        emailSender.sendEmail(email, RESERVATION_CANCELLATION_CONFIRMATION_SUBJECT, body);
    }

    @Override
    public void sendShopCreatedApprovalNeeded(Shop shop) {
        String body = loadTemplate("/email/shop-created-approval-needed.txt")
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_CREATED_APPROVAL_NEEDED_SUBJECT, body);
    }

    @Override
    public void sendShopApproved(Shop shop) {
        String body = loadTemplate("/email/shop-approved.txt")
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_APPROVED_SUBJECT, body);
    }

    @Override
    public void sendShopApprovalRevoked(Shop shop) {
        String body = loadTemplate("/email/shop-disapproved.txt")
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName());

        emailSender.sendEmail(shop.getEmail(), SHOP_DISAPPROVED_SUBJECT, body);
    }

    @Override
    public void sendAdminShopApprovalNeeded(List<Admin> admins, Shop shop) {
        if (admins.isEmpty()) {
            // Nothing to do here
            return;
        }

        String body = loadTemplate("/email/admin-shop-approval-needed.txt")
            .replace(EmailTemplateConstants.SHOP_NAME, shop.getName())
            .replace(EmailTemplateConstants.OWNER_NAME, shop.getOwnerName())
            .replace(EmailTemplateConstants.SHOP_EMAIL_ADDRESS, shop.getEmail())
            .replace(EmailTemplateConstants.SHOP_STREET, shop.getStreet())
            .replace(EmailTemplateConstants.SHOP_ZIP_CODE, shop.getZipCode())
            .replace(EmailTemplateConstants.SHOP_CITY, shop.getCity())
            .replace(EmailTemplateConstants.SHOP_ADDRESS_SUPPLEMENT, shop.getAddressSupplement())
            .replace(EmailTemplateConstants.SHOP_WEBSITE, Null.or(shop.getWebsite(), ""))
            .replace(EmailTemplateConstants.SHOP_DETAILS, shop.getDetails())
            .replace(EmailTemplateConstants.ADMIN_UI_LINK, config.getAdminUiLink());

        emailSender.sendEmails(Lists.map(admins, Admin::getEmail), ADMIN_SHOP_APPROVAL_NEEDED, body);
    }

    private String loadTemplate(String location) {
        try (InputStream stream = EmailServiceImpl.class.getResourceAsStream(location)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SendEmailException(String.format("Failed to read resource '%s'", location), e);
        }
    }
}
