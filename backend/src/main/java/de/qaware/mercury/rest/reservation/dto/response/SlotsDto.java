package de.qaware.mercury.rest.reservation.dto.response;

import de.qaware.mercury.business.reservation.Slots;
import de.qaware.mercury.util.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDto {
    private String start;
    private String end;
    private List<SlotDayDto> days;

    /**
     * Converts the given Slots to a SlotsDto.
     *
     * @param slots     slots
     * @param isHoliday predicate which is called with a date to determine if this date is a holiday
     * @return SlotsDto
     */
    public static SlotsDto of(Slots slots, Predicate<LocalDate> isHoliday) {
        return new SlotsDto(
            slots.getStart().toString(),
            slots.getEnd().toString(),
            Lists.map(slots.getDays(), s -> SlotDayDto.of(s, isHoliday))
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotDayDto {
        private String date;
        private String dayOfWeek;
        private boolean holiday;
        private List<SlotDto> slots;

        public static SlotDayDto of(Slots.SlotDay slotDay, Predicate<LocalDate> isHoliday) {
            return new SlotDayDto(
                slotDay.getDate().toString(), slotDay.getDate().getDayOfWeek().name(), isHoliday.test(slotDay.getDate()),
                Lists.map(slotDay.getSlots(), SlotDto::of)
            );
        }
    }
}
