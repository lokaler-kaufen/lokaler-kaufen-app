package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.login.ReservationCancellationToken
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.reservation.*
import de.qaware.mercury.business.shop.*
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.reservation.ReservationRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

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

    def "Cancel reservation as shop"() {
        setup:
        ReservationCancellationToken cancellationToken = new ReservationCancellationToken("cancellation-token")
        Reservation.Id reservationId = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ReservationCancellationSide cancellationSide = ReservationCancellationSide.SHOP
        ReservationCancellation cancellation = new ReservationCancellation(reservationId, cancellationSide)
        Reservation reservation = createTestReservation(reservationId, shopId)
        Shop shop = createTestShop(shopId)

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
        Shop shop = createTestShop(shopId)

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

    private Shop createTestShop(Shop.Id shopId) {
        new Shop.ShopBuilder()
            .id(shopId)
            .name("name")
            .ownerName("owner name")
            .email("email")
            .street("street")
            .zipCode("zip code")
            .city("city")
            .addressSupplement("address supplement")
            .contacts(new HashMap<ContactType, String>())
            .enabled(true)
            .approved(true)
            .geoLocation(new GeoLocation(47, 12))
            .details("details")
            .website("https://website.com")
            .slotConfig(createSlotConfig())
            .created(ZonedDateTime.now())
            .updated(ZonedDateTime.now())
            .build()
    }

    private static SlotConfig createSlotConfig() {
        return new SlotConfig(
            15,
            15,
            new DayConfig(LocalTime.parse("10:00"), LocalTime.parse("11:00")),
            new DayConfig(LocalTime.parse("11:30"), LocalTime.parse("12:30")),
            new DayConfig(LocalTime.parse("13:00"), LocalTime.parse("14:00")),
            new DayConfig(LocalTime.parse("14:30"), LocalTime.parse("15:30")),
            new DayConfig(LocalTime.parse("16:00"), LocalTime.parse("17:00")),
            new DayConfig(LocalTime.parse("17:30"), LocalTime.parse("18:30")),
            new DayConfig(LocalTime.parse("19:00"), LocalTime.parse("20:00"))
        )
    }
}
