package de.qaware.mercury.storage.reservation.impl

import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.business.reservation.ReservationNotFoundException
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.test.time.TestClock
import org.springframework.dao.EmptyResultDataAccessException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime
import java.time.ZonedDateTime

class JpaReservationRepositoryImplSpec extends Specification {

    private ReservationDataRepository dataRepository
    private Clock clock = new TestClock()

    @Subject
    private JpaReservationRepositoryImpl repository

    def setup() {
        dataRepository = Mock(ReservationDataRepository)
        repository = new JpaReservationRepositoryImpl(dataRepository, clock)
    }

    def "Inserts reservation"() {
        setup:
        Reservation.Id id = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Reservation reservation = new Reservation(
            id,
            shopId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "contact",
            "email",
            "name",
            ContactType.FACETIME,
            true,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )
        ReservationEntity entity = ReservationEntity.of(reservation)

        when:
        repository.insert(reservation)

        then:
        dataRepository.save(_) >> { passedEntity ->
            passedEntity == entity
        }
    }

    def "Finds reservations for shop"() {
        setup:
        Reservation.Id id = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        LocalDateTime start = LocalDateTime.now()
        LocalDateTime end = LocalDateTime.now()
        Reservation reservation = new Reservation(
            id,
            shopId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "contact",
            "email",
            "name",
            ContactType.FACETIME,
            true,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )
        List<Reservation> reservationList = List.of(reservation)
        ReservationEntity entity = ReservationEntity.of(reservation)
        List<ReservationEntity> entityList = List.of(entity)

        when:
        List<Reservation> result = repository.findReservationsForShop(shopId, start, end)

        then:
        result == reservationList
        1 * dataRepository.findReservationsForShop(shopId.getId(), start, end) >> entityList
    }

    def "Finds reservation by id"() {
        setup:
        Reservation.Id id = Reservation.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Reservation reservation = new Reservation(
            id,
            shopId,
            LocalDateTime.now(),
            LocalDateTime.now(),
            "contact",
            "email",
            "name",
            ContactType.FACETIME,
            true,
            ZonedDateTime.now(),
            ZonedDateTime.now()
        )
        ReservationEntity entity = ReservationEntity.of(reservation)

        when:
        Reservation result = repository.findById(id)

        then:
        result == reservation
        1 * dataRepository.findById(id.getId()) >> Optional.of(entity)
    }

    def "Deletes a reservation by its id"() {
        setup:
        Reservation.Id id = Reservation.Id.of(UUID.randomUUID())

        when:
        repository.deleteById(id)

        then:
        1 * dataRepository.deleteById(id.getId())
    }

    def "Deletion throws if reservation was not found"() throws ReservationNotFoundException {
        setup:
        Reservation.Id id = Reservation.Id.of(UUID.randomUUID())

        when:
        repository.deleteById(id)

        then:
        1 * dataRepository.deleteById(id.getId()) >> {
            throw new EmptyResultDataAccessException(0)
        }
        thrown ReservationNotFoundException
    }

    def "Repository gets called with current time"() {
        setup:
        LocalDateTime until = LocalDateTime.of(2020, 03, 10, 0, 0)
        ZonedDateTime current = ZonedDateTime.now()

        when:
        int result = repository.anonymizeExpired(until, current)

        then:
        result == 10
        1 * dataRepository.anonymizeExpired(until, "<anonymized>", current) >> 10
    }
}
