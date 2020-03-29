package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.business.reservation.ReservationService
import de.qaware.mercury.business.reservation.Slot
import de.qaware.mercury.business.reservation.SlotService
import de.qaware.mercury.business.reservation.Slots
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.shop.SlotConfig
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.reservation.ReservationRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

@ContextConfiguration(classes = ReservationServiceImpl)
class ReservationServiceImplSpec extends Specification {

    @Autowired
    ReservationService reservationService

    @SpringBean
    SlotService slotService = Mock()
    @SpringBean
    ReservationRepository reservationRepository = Mock()
    @SpringBean
    Clock clock = Mock()
    @SpringBean
    UUIDFactory uuidFactory = Mock()
    @SpringBean
    EmailService emailService = Mock()
    @SpringBean
    TokenService tokenService = Mock()
    @SpringBean
    ShopService shopService = Mock()

    def "List available slots for shop"() {
        given:
        LocalDate today = LocalDateTime.now().toLocalDate()
        Shop.Id id = Shop.Id.of(UUID.randomUUID())
        Shop shop = new Shop.ShopBuilder().id(id).slotConfig(SlotConfig.builder().build()).build()

        when:
        Slots slots = reservationService.listSlots(shop, 1)

        then:
        clock.today() >> today
        reservationRepository.findReservationsForShop(id, today.atTime(0, 0), today.atTime(23, 59)) >> []
        slotService.generateSlots(today, today, shop.getSlotConfig(), []) >> []
        slots.slots.isEmpty()
    }

    def "Create Reservation for Shop"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        // Slot length is 15 minutes
        SlotConfig slotConfig = SlotConfig.builder().timePerSlot(15).timeBetweenSlots(5).build()
        // Shop supports WHATSAPP contact
        Shop shop = new Shop.ShopBuilder().id(shopId).slotConfig(slotConfig).contacts([(ContactType.WHATSAPP): "1"]).build()
        Slot.Id slotId = Slot.Id.of(now)
        UUID reservationId = UUID.fromString('15ff0a8c-d29a-4e5e-a7fe-5487b79e92ac')
        uuidFactory.create() >> reservationId

        when:
        reservationService.createReservation(shop, slotId, ContactType.WHATSAPP, 'Contact', 'Spock', 'test@lokaler.kaufen')

        then:
        // Check for existing reservations
        1 * reservationRepository.findReservationsForShop(shopId, now, now.plusMinutes(15)) >> []
        // Check if the slot matches the slot configuration
        1 * slotService.isValidSlot(now, now.plusMinutes(15), slotConfig) >> true
        // Stores the reservation in the database
        1 * reservationRepository.insert(_)
        // Sends an email to the customer
        1 * emailService.sendCustomerReservationConfirmation(shop, 'test@lokaler.kaufen', 'Spock', now, now.plusMinutes(15), ContactType.WHATSAPP, 'Contact', new Reservation.Id(reservationId))
        // Sends an email to the shop
        1 * emailService.sendShopNewReservation(shop, 'Spock', now, now.plusMinutes(15), ContactType.WHATSAPP, 'Contact', new Reservation.Id(reservationId))
    }
}
