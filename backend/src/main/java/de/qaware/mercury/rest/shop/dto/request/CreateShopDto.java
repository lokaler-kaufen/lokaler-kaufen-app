package de.qaware.mercury.rest.shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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
    @Size(min = 14)
    private String password;
    // Maps from WHATSAPP -> Telephone number, for example
    @NotEmpty
    private List<CreateContactDto> contacts;
    @Valid
    private SlotConfigDto slots;
}