package de.qaware.mercury.mercury.rest.shop.dto.request;

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
public class CreateShopRequestDto {
    @NotBlank
    // TODO: Validate pattern
    private String ownerName;
    @NotBlank
    // TODO: Validate pattern
    private String name;
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
    // TODO: Validate pattern
    private String addressSupplement;
    @NotBlank
    // TODO: Validate pattern
    private String details;
    @Nullable
    private String website;
    @NotBlank
    // TODO: Validate pattern
    private String password;
    // Maps from WHATSAPP -> Telephone number, for example
    @NotEmpty
    private Map<String, String> contactTypes;
    @Valid
    private SlotConfigDto slots;
}