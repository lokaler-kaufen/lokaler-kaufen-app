package de.qaware.mercury.rest.reservation.dto.response;

import de.qaware.mercury.business.reservation.Slots;
import de.qaware.mercury.util.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDto {
    private String start;
    private String end;
    private List<SlotDayDto> days;

    public static SlotsDto of(Slots slots) {
        return new SlotsDto(
            slots.getStart().toString(),
            slots.getEnd().toString(),
            Lists.map(slots.getDays(), SlotDayDto::of)
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

        public static SlotDayDto of(Slots.SlotDay slotDay) {
            return new SlotDayDto(
                // TODO: Holiday stuff
                slotDay.getDate().toString(), slotDay.getDate().getDayOfWeek().name(), false, Lists.map(slotDay.getSlots(), SlotDto::of)
            );
        }
    }
}
