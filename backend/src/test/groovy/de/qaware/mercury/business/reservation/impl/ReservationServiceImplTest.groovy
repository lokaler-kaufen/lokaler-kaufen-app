package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.login.ReservationCancellationToken
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.business.reservation.ReservationCancellation
import de.qaware.mercury.business.reservation.ReservationCancellationSide
import de.qaware.mercury.business.reservation.ReservationNotFoundException
import de.qaware.mercury.business.reservation.ReservationService
import de.qaware.mercury.business.reservation.Slot
import de.qaware.mercury.business.reservation.SlotService
import de.qaware.mercury.business.reservation.Slots
import de.qaware.mercury.business.shop.Breaks
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.shop.SlotConfig
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.reservation.ReservationRepository
import de.qaware.mercury.test.builder.SlotConfigBuilder
import de.qaware.mercury.test.fixtures.BreaksFixtures
import de.qaware.mercury.test.fixtures.ShopFixtures
import de.qaware.mercury.test.fixtures.SlotConfigFixtures
import de.qaware.mercury.test.time.TestClock
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

class ReservationServiceImplTest extends Specification {
    SlotService slotService = Mock()
    ReservationRepository reservationRepository = Mock()
    TestClock clock = new TestClock()
    UUIDFactory uuidFactory = Mock()
    EmailService emailService = Mock()
    TokenService tokenService = Mock()
    ShopService shopService = Mock()

    @Subject
    ReservationService reservationService = new ReservationServiceImpl(slotService, reservationRepository, clock, uuidFactory, emailService, tokenService, shopService)

    def "List available slots for shop"() {
        given:
        clock.setNow(ZonedDateTime.now())
        LocalDate today = clock.today()
        Shop.Id id = Shop.Id.of(UUID.randomUUID())
        Shop shop = new Shop.ShopBuilder().id(id).slotConfig(new SlotConfigBuilder().build()).build()

        when:
        Slots slots = reservationService.listSlots(shop, 1)

        then:
        reservationRepository.findReservationsForShop(id, today.atTime(0, 0), today.atTime(23, 59)) >> []
        slotService.generateSlots(today, today, shop.getSlotConfig(), []) >> new Slots(today, today, [])
        slots.days.isEmpty()
        shopService.findBreaks(shop) >> Breaks.none()
    }

    def "Create Reservation for Shop"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        // Slot length is 15 minutes
        SlotConfig slotConfig = new SlotConfigBuilder().setTimePerSlot(15).setTimeBetweenSlots(5).build()
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

    def "Cancel reservation as shop"() {
        setup:
        ReservationCancellationToken cancellationToken = new ReservationCancellationToken("cancellation-token")
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ReservationCancellationSide cancellationSide = ReservationCancellationSide.SHOP
        ReservationCancellation cancellation = new ReservationCancellation(reservationId, cancellationSide)
        Reservation reservation = createTestReservation(reservationId, shopId)
        Shop shop = ShopFixtures.create()

        when:
        reservationService.cancelReservation(cancellationToken)

        then:
        1 * tokenService.verifyReservationCancellationToken(cancellationToken) >> cancellation
        1 * reservationRepository.findById(reservationId) >> reservation
        1 * shopService.findByIdOrThrow(shopId) >> shop
        1 * reservationRepository.deleteById(reservationId)
        1 * emailService.sendReservationCancellationToCustomer(shop, reservation)
        1 * emailService.sendReservationCancellationConfirmation(shop.getEmail(), reservation)
    }

    def "Cancel reservation as customer"() {
        setup:
        ReservationCancellationToken cancellationToken = new ReservationCancellationToken("cancellation-token")
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ReservationCancellationSide cancellationSide = ReservationCancellationSide.CUSTOMER
        ReservationCancellation cancellation = new ReservationCancellation(reservationId, cancellationSide)
        Reservation reservation = createTestReservation(reservationId, shopId)
        Shop shop = ShopFixtures.create();

        when:
        reservationService.cancelReservation(cancellationToken)

        then:
        1 * tokenService.verifyReservationCancellationToken(cancellationToken) >> cancellation
        1 * reservationRepository.findById(reservationId) >> reservation
        1 * shopService.findByIdOrThrow(shopId) >> shop
        1 * reservationRepository.deleteById(reservationId)
        1 * emailService.sendReservationCancellationToShop(shop, reservation)
        1 * emailService.sendReservationCancellationConfirmation(reservation.getEmail(), reservation)
    }

    def "Throws if reservation cannot be found"() {
        setup:
        ReservationCancellationToken cancellationToken = new ReservationCancellationToken("cancellation-token")
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        ReservationCancellationSide cancellationSide = ReservationCancellationSide.CUSTOMER
        ReservationCancellation cancellation = new ReservationCancellation(reservationId, cancellationSide)

        when:
        reservationService.cancelReservation(cancellationToken)

        then:
        1 * tokenService.verifyReservationCancellationToken(cancellationToken) >> cancellation
        1 * reservationRepository.findById(reservationId) >> null
        thrown ReservationNotFoundException
    }

    def "test shop breaks"() {
        given: "a shop, open monday from 10:00 - 13:00, pause from 11:00 - 12:00"
        // This is a monday
        clock.setNow(ZonedDateTime.parse("2020-04-20T07:00:00+02:00[Europe/Berlin]"))

        // We want to test against a real slot service, not the mocked one
        ReservationService sut = new ReservationServiceImpl(new SlotServiceImpl(clock), reservationRepository, clock, uuidFactory, emailService, tokenService, shopService)

        Shop shop = ShopFixtures.create().withSlotConfig(
            SlotConfigFixtures.monday(30, 30, LocalTime.of(10, 0), LocalTime.of(13, 0))
        )
        // No reservations
        reservationRepository.findReservationsForShop(shop.id, _, _) >> []
        // Break from 11:00 to 12:00
        shopService.findBreaks(shop) >> BreaksFixtures.monday(LocalTime.of(11, 0), LocalTime.of(12, 0))

        when: "we list the slots for today"
        Slots slots = sut.listSlots(shop, 1)

        then: "we get 3 slots, and one is marked as not available"
        def today = clock.today()
        Slots.SlotDay monday = slots.days[0]
        // Slots should be: 10:00 - 10:30 | 11:00 - 11:30 (break) | 12:00 - 12:30
        monday.slots.size() == 3

        monday.slots[0].start == today.atTime(10, 0)
        monday.slots[0].end == today.atTime(10, 30)
        monday.slots[0].available

        monday.slots[1].start == today.atTime(11, 0)
        monday.slots[1].end == today.atTime(11, 30)
        !monday.slots[1].available // Pause!

        monday.slots[2].start == today.atTime(12, 0)
        monday.slots[2].end == today.atTime(12, 30)
        monday.slots[2].available
    }

    private static Reservation createTestReservation(Reservation.Id reservationId, Shop.Id shopId) {
        new Reservation.ReservationBuilder()
            .id(reservationId)
            .shopId(shopId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .contact("contact")
            .email("email")
            .name("name")
            .contactType(ContactType.FACETIME)
            .anonymized(true)
            .created(ZonedDateTime.now())
            .updated(ZonedDateTime.now())
            .build()
    }
}
