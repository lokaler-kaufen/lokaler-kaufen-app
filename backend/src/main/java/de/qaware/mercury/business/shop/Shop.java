package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.location.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class Shop {
    Id id;
    String name;
    String slug;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Map<ContactType, String> contacts;
    boolean enabled;
    @With
    boolean approved;
    @With
    @Nullable
    Image.Id imageId;
    @With
    @Nullable
    String shopColor;
    boolean autoColorEnabled;
    GeoLocation geoLocation;
    String details;
    @Nullable
    String website;
    @With
    SlotConfig slotConfig;
    SocialLinks socialLinks;
    @With
    ZonedDateTime created;
    @With
    ZonedDateTime updated;

    @Value(staticConstructor = "of")
    @SuppressWarnings("java:S1700") // Shut up SonarQube
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
