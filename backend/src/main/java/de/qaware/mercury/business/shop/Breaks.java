package de.qaware.mercury.business.shop;

import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
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

        private Break(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }

        public static Break of(LocalTime start, LocalTime end) {
            if (end.isBefore(start)) {
                throw new InvalidBreakException(String.format("end is before start. start = %s, end = %s", start, end));
            }
            return new Break(start, end);
        }
    }

    public Map<DayOfWeek, Set<Break>> groupedByDayOfWeek() {
        Map<DayOfWeek, Set<Break>> result = new EnumMap<>(DayOfWeek.class);

        result.put(DayOfWeek.MONDAY, monday);
        result.put(DayOfWeek.TUESDAY, tuesday);
        result.put(DayOfWeek.WEDNESDAY, wednesday);
        result.put(DayOfWeek.THURSDAY, thursday);
        result.put(DayOfWeek.FRIDAY, friday);
        result.put(DayOfWeek.SATURDAY, saturday);
        result.put(DayOfWeek.SUNDAY, sunday);

        return result;
    }

    public Set<Break> at(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return monday;
            case TUESDAY:
                return tuesday;
            case WEDNESDAY:
                return wednesday;
            case THURSDAY:
                return thursday;
            case FRIDAY:
                return friday;
            case SATURDAY:
                return saturday;
            case SUNDAY:
                return sunday;
            default:
                throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        }
    }

    public static Breaks none() {
        return new Breaks(Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of(), Set.of());
    }

    public static Breaks everyday(LocalTime start, LocalTime end) {
        Break aBreak = Break.of(start, end);

        return new Breaks(
            Set.of(aBreak), Set.of(aBreak), Set.of(aBreak), Set.of(aBreak), Set.of(aBreak), Set.of(aBreak), Set.of(aBreak)
        );
    }
}
