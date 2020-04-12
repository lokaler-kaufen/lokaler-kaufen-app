package de.qaware.mercury.business.shop;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class Breaks {
    Set<Break> monday;
    Set<Break> tuesday;
    Set<Break> wednesday;
    Set<Break> thursday;
    Set<Break> friday;
    Set<Break> saturday;
    Set<Break> sunday;

    @Value
    public static class Break {
        LocalDateTime start;
        LocalDateTime end;
    }

    public static Breaks none() {
        return new Breaks(Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of());
    }
}
