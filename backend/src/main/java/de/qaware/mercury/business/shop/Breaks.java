package de.qaware.mercury.business.shop;

import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
        LocalTime start;
        LocalTime end;
    }

    public Map<DayOfWeek, Set<Break>> groupedByDayOfWeek() {
        Map<DayOfWeek, Set<Break>> result = new HashMap<>();

        result.put(DayOfWeek.MONDAY, monday);
        result.put(DayOfWeek.TUESDAY, tuesday);
        result.put(DayOfWeek.WEDNESDAY, wednesday);
        result.put(DayOfWeek.THURSDAY, thursday);
        result.put(DayOfWeek.FRIDAY, friday);
        result.put(DayOfWeek.SATURDAY, saturday);
        result.put(DayOfWeek.SUNDAY, sunday);

        return result;
    }

    public static Breaks none() {
        return new Breaks(Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of());
    }
}
