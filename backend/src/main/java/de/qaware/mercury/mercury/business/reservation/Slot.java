package de.qaware.mercury.mercury.business.reservation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Value
public class Slot {
    Id id;
    LocalDateTime start;
    LocalDateTime end;
    boolean available;

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Id {
        String id;

        public LocalDateTime toLocalDateTime() {
            return LocalDateTime.parse(id);
        }

        public static Id of(LocalDateTime start) {
            return new Id(start.toString());
        }

        public static Id parse(String input) {
            try {
                LocalDateTime.parse(input); // Validates input
            } catch (DateTimeParseException e) {
                throw new InvalidSlotIdException(input, e);
            }
            return new Id(input);
        }
    }
}
