package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.location.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class Shop {
    Id id;
    String name;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Set<ShopContact> contacts;
    boolean enabled;
    @With
    boolean approved;
    GeoLocation geoLocation;
    String details;
    @Nullable
    String website;
    SlotConfig slotConfig;
    @With
    ZonedDateTime created;
    @With
    ZonedDateTime updated;

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
