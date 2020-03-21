package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Slot;
import de.qaware.mercury.mercury.business.shop.Slots;
import de.qaware.mercury.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShopRequestDto {
    private String ownerName;
    private String name;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private String details;
    @Nullable
    private String website;
    private String password;
    // Maps from WHATSAPP -> Telephone number, for example
    private Map<String, String> contactTypes;
    private SlotsDto slots;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotsDto {
        private int timePerSlot;
        private int timeBetweenSlots;

        @Nullable
        private SlotDto monday;
        @Nullable
        private SlotDto tuesday;
        @Nullable
        private SlotDto wednesday;
        @Nullable
        private SlotDto thursday;
        @Nullable
        private SlotDto friday;
        @Nullable
        private SlotDto saturday;
        @Nullable
        private SlotDto sunday;

        public Slots toSlots() {
            return new Slots(
                timePerSlot, timeBetweenSlots, Null.map(monday, SlotDto::toSlot), Null.map(tuesday, SlotDto::toSlot),
                Null.map(wednesday, SlotDto::toSlot), Null.map(thursday, SlotDto::toSlot), Null.map(friday, SlotDto::toSlot),
                Null.map(saturday, SlotDto::toSlot), Null.map(sunday, SlotDto::toSlot)
            );
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotDto {
        private String start;
        private String end;

        public Slot toSlot() {
            return new Slot(parse(start), parse(end));
        }

        private LocalTime parse(String time) {
            String[] parts = time.split(":");
            return LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }
}