package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.uuid.UUIDFactory;
import lombok.Value;

import java.util.UUID;

@Value
public
class ShopContact {
    Id id;
    ContactType contactType;
    String data;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Shop.Id parse(String input) {
            return Shop.Id.of(UUID.fromString(input));
        }

        public static Id random(UUIDFactory uuidFactory) {
            return Id.of(uuidFactory.create());
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
