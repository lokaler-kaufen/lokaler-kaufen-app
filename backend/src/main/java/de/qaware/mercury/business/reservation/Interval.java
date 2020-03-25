package de.qaware.mercury.business.reservation;

import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class Interval {
    LocalDateTime start;
    LocalDateTime end;

    public boolean overlaps(Interval other) {
        // See https://stackoverflow.com/questions/14210980/interval-intersection
        return end.isAfter(other.start) && start.isBefore(other.end);
    }
}
