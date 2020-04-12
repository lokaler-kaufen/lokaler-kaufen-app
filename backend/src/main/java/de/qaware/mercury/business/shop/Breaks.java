package de.qaware.mercury.business.shop;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class Breaks {
    List<Break> monday;
    List<Break> tuesday;
    List<Break> wednesday;
    List<Break> thursday;
    List<Break> friday;
    List<Break> saturday;
    List<Break> sunday;

    @Value
    public static class Break {
        LocalDateTime start;
        LocalDateTime end;
    }

    public static Breaks none() {
        return new Breaks(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }
}
