package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.location.Location;
import lombok.Value;
import lombok.With;

import java.util.UUID;

@Value
public class Shop {
    Id id;
    String name;
    @With
    boolean enabled;
    Location location;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Id parse(String input) {
            return Id.of(UUID.fromString(input));
        }
    }
}
