package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Shop {
    Id id;
    String name;
    boolean enabled;
    Location location;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;
    }
}
