package de.qaware.mercury.business.login;

import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.uuid.UUIDFactory;
import lombok.Value;
import lombok.With;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class ShopLogin {
    Id id;
    Shop.Id shopId;
    String email;
    @With
    String passwordHash;
    @With
    ZonedDateTime created;
    @With
    ZonedDateTime updated;

    @Value(staticConstructor = "of")
    @SuppressWarnings("java:S1700") // Shut up SonarQube
    public static class Id {
        UUID id;

        public static Id random(UUIDFactory uuidFactory) {
            return Id.of(uuidFactory.create());
        }

        public static Id parse(String input) {
            return Id.of(UUID.fromString(input));
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
