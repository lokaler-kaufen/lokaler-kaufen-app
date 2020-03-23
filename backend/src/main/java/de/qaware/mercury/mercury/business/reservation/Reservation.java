package de.qaware.mercury.mercury.business.reservation;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class Reservation {
    Id id;
    Shop.Id shopId;
    LocalDateTime start;
    LocalDateTime end;
    String contact;
    String email;
    ContactType contactType;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Id random(UUIDFactory uuidFactory) {
            return Id.of(uuidFactory.create());
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
