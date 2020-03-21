package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import lombok.Value;

import java.util.UUID;

@Value
public class ShopLogin {
    Id id;
    Shop.Id shopId;
    String email;
    String passwordHash;

    @Value(staticConstructor = "of")
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
