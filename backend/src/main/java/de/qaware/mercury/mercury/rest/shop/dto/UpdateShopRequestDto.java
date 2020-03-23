package de.qaware.mercury.mercury.rest.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopRequestDto {
    @NotBlank
    // TODO: Validate pattern
    private String name;
    @NotBlank
    // TODO: Validate pattern
    private String ownerName;
    @NotBlank
    // TODO: Validate pattern
    private String street;
    @NotBlank
    // TODO: Validate pattern
    private String zipCode;
    @NotBlank
    // TODO: Validate pattern
    private String city;
    @NotNull
    private String addressSupplement;
    @NotNull
    private String details;
    @Nullable
    private String website;
    // Maps from WHATSAPP -> Telephone number, for example
    @NotEmpty
    private Map<String, String> contactTypes;
    @Valid
    private SlotConfigDto slots;
}
