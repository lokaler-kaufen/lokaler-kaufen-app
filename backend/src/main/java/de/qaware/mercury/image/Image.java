package de.qaware.mercury.image;

import lombok.Value;

import java.util.UUID;

public class Image {
    Id id;

    @Value(staticConstructor = "of")
    public static class Id {
        UUID id;

        public static Id parse(String input) {
            try {
                return Id.of(UUID.fromString(input));
            } catch (IllegalArgumentException e) {
                throw new InvalidImageIdException(input, e);
            }
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
