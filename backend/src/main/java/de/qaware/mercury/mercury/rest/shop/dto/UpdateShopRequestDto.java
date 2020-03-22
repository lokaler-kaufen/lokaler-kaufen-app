package de.qaware.mercury.mercury.rest.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopRequestDto {
    private String name;
    private String ownerName;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private String details;
    @Nullable
    private String website;
    // Maps from WHATSAPP -> Telephone number, for example
    private Map<String, String> contactTypes;
    private SlotConfigDto slots;
}
