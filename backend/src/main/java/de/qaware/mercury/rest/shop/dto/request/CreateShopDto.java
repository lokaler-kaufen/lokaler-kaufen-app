package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.rest.validation.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShopDto {
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
    @Size(min = 5, max = 5)
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
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
    private String password;
    // Maps from WHATSAPP -> Telephone number, for example
    @NotEmpty
    private Map<String, String> contacts;
    @Valid
    private SlotConfigDto slots;
}