package de.qaware.mercury.rest.shop.dto.request;

import de.qaware.mercury.business.shop.ContactCreation;
import de.qaware.mercury.business.shop.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactDto {
    @NotBlank
    private ContactType contactType;

    @NotBlank
    private String data;

    public ContactCreation toContactCreation() {
        return ContactCreation
            .builder()
            .contactType(contactType)
            .data(data)
            .build();
    }
}
