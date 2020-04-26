package de.qaware.mercury.business.email.impl

import de.qaware.mercury.business.email.EmailSender
import de.qaware.mercury.business.email.ICalendarService
import de.qaware.mercury.business.i18n.DateTimeI18nService
import de.qaware.mercury.business.login.PasswordResetToken
import de.qaware.mercury.business.login.ReservationCancellationToken
import de.qaware.mercury.business.login.ShopCreationToken
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.business.reservation.ReservationCancellationSide
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.Shop
import org.springframework.context.MessageSource
import spock.lang.Specification

import java.time.LocalDateTime

class EmailServiceImplSpec extends Specification {
    static final String EMAIL = 'test@lokaler.kaufen'

    EmailServiceImpl emailService
    EmailSender emailSender = Mock()
    EmailConfigurationProperties properties = Mock()
    TokenService tokenService = Mock()
    DateTimeI18nService i18nService = Mock()
    MessageSource messageSource = Mock()
    ICalendarService iCalendarService = Mock()

    void setup() {
        emailService = new EmailServiceImpl(emailSender, properties, tokenService, i18nService, messageSource, iCalendarService)
    }

    def "Send Shop creation link"() {
        when:
        emailService.sendShopCreationLink(EMAIL)

        then:
        1 * tokenService.createShopCreationToken(EMAIL) >> ShopCreationToken.of("test")
        1 * properties.getCreationLinkTemplate() >> '{{ token }}'
        1 * emailSender.sendEmail(EMAIL, _, { it != null })
        1 * messageSource.getMessage({ it != null }, _, { it != null })
    }

    def "Send customer reservation confirmation"() {
        given:
        Shop shop = Shop.builder().name('BoozShop').ownerName('Spock').build()
        LocalDateTime slot = LocalDateTime.now()
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        iCalendarService.newReservation(reservationId, slot, slot, _, _) >> ""

        when:
        emailService.sendCustomerReservationConfirmation(shop, 'test@shop.de', 'Test Shop', slot, slot, ContactType.WHATSAPP, 'Spock', reservationId)

        then:
        1 * i18nService.formatDate(_) >> '24.12.12412'
        2 * i18nService.formatTime(_) >> '00:00:00'
        1 * properties.getReservationCancellationLinkTemplate() >> '{{ token }}'
        1 * tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.CUSTOMER, slot) >> new ReservationCancellationToken("test")
        1 * messageSource.getMessage({ it != null }, _, { it != null })
        noExceptionThrown()
    }

    def "Send password reset email"() {
        given:
        PasswordResetToken token = PasswordResetToken.of(UUID.randomUUID().toString())

        when:
        emailService.sendShopPasswordResetEmail('test@loakler.kaufen', token)

        then:
        1 * properties.getShopPasswordResetLinkTemplate() >> '{{ token }}'
        1 * messageSource.getMessage({ it != null }, _, { it != null })
        noExceptionThrown()
    }

    def "Send shop news reservation"() {
        given:
        Shop shop = Shop.builder().ownerName('Spock').build()
        LocalDateTime slot = LocalDateTime.now()
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        iCalendarService.newReservation(reservationId, slot, slot, _, _) >> ""

        when:
        emailService.sendShopNewReservation(shop, 'Test Shop', slot, slot, ContactType.WHATSAPP, 'Spock', reservationId)

        then:
        1 * i18nService.formatDate(_) >> '24.12.12412'
        2 * i18nService.formatTime(_) >> '00:00:00'
        1 * properties.getReservationCancellationLinkTemplate() >> '{{ token }}'
        1 * tokenService.createReservationCancellationToken(reservationId, ReservationCancellationSide.SHOP, slot) >> new ReservationCancellationToken("test")
        1 * messageSource.getMessage({ it != null }, _, { it != null })
        noExceptionThrown()
    }
}
