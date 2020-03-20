package de.qaware.mercury.mercury.business;

import lombok.Value;

import java.util.UUID;

@Value
public class Shop {
    Id id;
    String name;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;
    }
}
