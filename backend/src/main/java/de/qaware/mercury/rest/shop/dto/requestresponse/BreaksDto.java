package de.qaware.mercury.rest.shop.dto.requestresponse;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.InvalidBreakException;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreaksDto {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @NotNull
    private List<BreakDto> monday;
    @NotNull
    private List<BreakDto> tuesday;
    @NotNull
    private List<BreakDto> wednesday;
    @NotNull
    private List<BreakDto> thursday;
    @NotNull
    private List<BreakDto> friday;
    @NotNull
    private List<BreakDto> saturday;
    @NotNull
    private List<BreakDto> sunday;

    public static BreaksDto of(Breaks breaks) {
        return new BreaksDto(
            Lists.map(breaks.getMonday(), BreakDto::of),
            Lists.map(breaks.getTuesday(), BreakDto::of),
            Lists.map(breaks.getWednesday(), BreakDto::of),
            Lists.map(breaks.getThursday(), BreakDto::of),
            Lists.map(breaks.getFriday(), BreakDto::of),
            Lists.map(breaks.getSaturday(), BreakDto::of),
            Lists.map(breaks.getSunday(), BreakDto::of)
        );
    }

    public Breaks toBreaks() {
        return new Breaks(
            Sets.map(monday, BreakDto::toBreak),
            Sets.map(tuesday, BreakDto::toBreak),
            Sets.map(wednesday, BreakDto::toBreak),
            Sets.map(thursday, BreakDto::toBreak),
            Sets.map(friday, BreakDto::toBreak),
            Sets.map(saturday, BreakDto::toBreak),
            Sets.map(sunday, BreakDto::toBreak)
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BreakDto {
        private String start;
        private String end;

        public Breaks.Break toBreak() {
            try {
                LocalTime startTime = LocalTime.parse(start, TIME_FORMATTER);
                LocalTime endTime = LocalTime.parse(end, TIME_FORMATTER);

                return Breaks.Break.of(startTime, endTime);
            } catch (DateTimeParseException e) {
                throw new InvalidBreakException(start, end, "Failed to parse time", e);
            }
        }

        public static BreakDto of(Breaks.Break aBreak) {
            return new BreakDto(
                TIME_FORMATTER.format(aBreak.getStart()), TIME_FORMATTER.format(aBreak.getEnd())
            );
        }
    }
}
