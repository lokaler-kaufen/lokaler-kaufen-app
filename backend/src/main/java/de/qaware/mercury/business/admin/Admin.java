package de.qaware.mercury.business.admin;

import de.qaware.mercury.business.uuid.UUIDFactory;
import lombok.Value;
import lombok.With;

import java.time.ZonedDateTime;
import java.util.UUID;

@Value
public class Admin {
    Id id;
    String email;
    String passwordHash;
    boolean emailOnShopApprovalNeeded;
    @With
    ZonedDateTime created;
    @With
    ZonedDateTime updated;

    @Value(staticConstructor = "of")
    @SuppressWarnings("java:S1700") // Shut up SonarQube
    public static class Id {
        UUID id;

        public static Id parse(String input) {
            return Id.of(UUID.fromString(input));
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
