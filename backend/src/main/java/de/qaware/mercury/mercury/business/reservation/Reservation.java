package de.qaware.mercury.mercury.business.reservation;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
public class Reservation {
    Id id;
    Shop.Id shopId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    String contactInformation;
    String email;
    ContactType contactType;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Reservation.Id parse(String input) {
            return Reservation.Id.of(UUID.fromString(input));
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
