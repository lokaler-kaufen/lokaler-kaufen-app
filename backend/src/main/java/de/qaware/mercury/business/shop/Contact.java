package de.qaware.mercury.business.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class Contact {
    Id id;
    Shop.Id shopId;
    ContactType contactType;
    String data;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Contact.Id parse(String input) {
            try {
                return Contact.Id.of(UUID.fromString(input));
            } catch (IllegalArgumentException e) {
                throw new InvalidShopIdException(input, e);
            }
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
