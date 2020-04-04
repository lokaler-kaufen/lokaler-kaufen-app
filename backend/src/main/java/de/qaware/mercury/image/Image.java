package de.qaware.mercury.image;

import de.qaware.mercury.business.uuid.UUIDFactory;
import lombok.Value;

import java.util.UUID;

@Value
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

        public static Id random(UUIDFactory uuidFactory) {
            return of(uuidFactory.create());
        }

        @Override
        public String toString() {
            return id.toString();
        }
    }
}
