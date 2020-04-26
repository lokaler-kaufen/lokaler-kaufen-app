package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.validation.EnumValue;
import de.qaware.mercury.rest.shop.dto.requestresponse.BreaksDto;
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto;
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
public class UpdateShopDto {
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
    @Size(min = 5, max = 5)
    private String zipCode;
    @NotBlank
    // TODO: Validate pattern
    private String city;
    @NotNull
    private String addressSupplement;
    @NotBlank
    private String details;
    @Nullable
    private String website;
    // Maps from WHATSAPP -> Telephone number, for example
    private boolean autoColorEnabled;
    @NotEmpty
    private Map<@EnumValue(enumClass = ContactType.class) String, String> contacts;
    @Valid
    private SlotConfigDto slots;
    @Valid
    @NotNull
    private SocialLinksDto socialLinks;
    @Valid
    @NotNull
    private BreaksDto breaks;
}
