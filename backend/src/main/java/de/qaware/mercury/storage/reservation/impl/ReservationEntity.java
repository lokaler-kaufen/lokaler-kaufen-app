package de.qaware.mercury.storage.reservation.impl;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
class ReservationEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID shopId;

    @Setter
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Setter
    @Column(nullable = false)
    private LocalDateTime endTime;

    @Setter
    @Column(nullable = false)
    private String contact;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String contactType;

    @Column(nullable = false)
    private boolean anonymized;

    @Column(nullable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime updated;

    public static ReservationEntity of(Reservation reservation) {
        return new ReservationEntity(
            reservation.getId().getId(),
            reservation.getShopId().getId(),
            reservation.getStart(),
            reservation.getEnd(),
            reservation.getContact(),
            reservation.getEmail(),
            reservation.getName(),
            reservation.getContactType().getId(),
            reservation.isAnonymized(),
            reservation.getCreated(),
            reservation.getUpdated()
        );
    }

    public Reservation toReservation() {
        return new Reservation(
            Reservation.Id.of(id),
            Shop.Id.of(shopId),
            startTime,
            endTime,
            contact,
            email,
            name,
            ContactType.parse(contactType),
            anonymized,
            created,
            updated
        );
    }
}
