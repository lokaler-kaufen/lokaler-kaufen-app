package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.UUID;

@Value
public class Shop {
    Id id;
    String name;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Map<ContactType, String> contactTypes;
    @With
    boolean enabled;
    GeoLocation geoLocation;
    String details;
    @Nullable
    String website;
    SlotConfig slotConfig;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Id parse(String input) {
            try {
                return Id.of(UUID.fromString(input));
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
