package de.qaware.mercury.mercury.rest.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotDto {
        private String start;
        private String end;
    }
}