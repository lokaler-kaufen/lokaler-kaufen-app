package de.qaware.mercury.business.reservation.impl

import de.qaware.mercury.business.reservation.Reservation
import de.qaware.mercury.business.reservation.ReservationService
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.reservation.ReservationRepository
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager

class ReservationServiceImplIntTest extends IntegrationTestSpecification {
    @Autowired
    ReservationService reservationService

    @Autowired
    ReservationRepository reservationRepository

    @Autowired
    UUIDFactory uuidFactory

    @Autowired
    ShopService shopService

    @Autowired
    Clock clock

    @Autowired
    EntityManager entityManager

    def "anonymized expired reservations"() {
        given: "a shop"
        Shop shop = shopService.create(ShopCreationFixtures.create())

        and: "a reservation made 2 days ago"
        Reservation.Id reservationIdToAnonymize = Reservation.Id.random(uuidFactory)
        reservationRepository.insert(new Reservation(
            reservationIdToAnonymize, shop.getId(), clock.now().minusDays(2).minusMinutes(15), clock.now().minusDays(2), "contact", "email@local.host",
            "name", ContactType.WHATSAPP, false, clock.nowZoned(), clock.nowZoned()
        ))
        and: "a reservation made now"
        Reservation.Id reservationIdToLeaveBe = Reservation.Id.random(uuidFactory)
        reservationRepository.insert(new Reservation(
            reservationIdToLeaveBe, shop.getId(), clock.now().minusMinutes(15), clock.now(), "contact", "email@local.host",
            "name", ContactType.WHATSAPP, false, clock.nowZoned(), clock.nowZoned()
        ))

        when: "we run the anonymizer job"
        int noOfAnonymizedReservations = reservationService.anonymizeExpired()

        // Needed to defeat the 1st level cache of Hibernate
        entityManager.flush()
        entityManager.clear()

        then: "1 reservation has been anonymized"
        noOfAnonymizedReservations == 1

        then: "the 2 days old reservation has been anonymized"
        Reservation anonymized = reservationRepository.findById(reservationIdToAnonymize)
        anonymized.isAnonymized()
        anonymized.getContact() == '<anonymized>'
        anonymized.getEmail() == '<anonymized>'
        anonymized.getName() == '<anonymized>'

        then: "the reservation from now hasn't been touched"
        Reservation notAnonymized = reservationRepository.findById(reservationIdToLeaveBe)
        !notAnonymized.isAnonymized()
        notAnonymized.getContact() == 'contact'
        notAnonymized.getEmail() == 'email@local.host'
        notAnonymized.getName() == 'name'
    }
}
