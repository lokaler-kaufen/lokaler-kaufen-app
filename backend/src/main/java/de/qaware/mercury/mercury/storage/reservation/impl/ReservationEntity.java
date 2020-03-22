package de.qaware.mercury.mercury.storage.reservation.impl;

import de.qaware.mercury.mercury.business.reservation.Reservation;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    private OffsetDateTime startTime;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime endTime;

    @Setter
    @Column(nullable = false)
    private String contact;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String contactType;

    public static ReservationEntity of(Reservation reservation) {
        return new ReservationEntity(
            reservation.getId().getId(),
            reservation.getShopId().getId(),
            reservation.getStart().atOffset(ZoneOffset.UTC),
            reservation.getEnd().atOffset(ZoneOffset.UTC),
            reservation.getContact(),
            reservation.getEmail(),
            reservation.getContactType().name()
        );
    }

    public Reservation toReservation() {
        return new Reservation(
            Reservation.Id.of(id),
            Shop.Id.of(shopId),
            startTime.toLocalDateTime(),
            endTime.toLocalDateTime(),
            contact,
            email,
            ContactType.valueOf(contactType)
        );
    }
}
