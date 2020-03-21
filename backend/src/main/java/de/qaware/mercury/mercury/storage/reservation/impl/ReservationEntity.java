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
    private String contactInformation;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false, columnDefinition = "contact_type")
    private ContactType contactType;

    public static ReservationEntity of(Reservation reservation) {
        return new ReservationEntity(
            reservation.getId().getId(),
            reservation.getShopId().getId(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getContactInformation(),
            reservation.getEmail(),
            reservation.getContactType()
        );
    }

    public Reservation toReservation() {
        return new Reservation(
            Reservation.Id.of(id),
            Shop.Id.of(shopId),
            startTime,
            endTime,
            contactInformation,
            email,
            contactType
        );
    }
}
