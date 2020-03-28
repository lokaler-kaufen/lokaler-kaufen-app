package de.qaware.mercury.rest.shop.dto.both;

import de.qaware.mercury.business.shop.Contact;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.util.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    @NotBlank
    @Pattern(regexp = Validation.CONTACT_ID)
    private String id;

    @NotBlank
    @Pattern(regexp = Validation.SHOP_ID)
    private String shopId;

    @NotBlank
    private ContactType contactType;

    @NotBlank
    private String data;

    public static ContactDto of(Contact contact) {
        return new ContactDto(
            contact.getId().toString(),
            contact.getShopId().toString(),
            contact.getContactType(),
            contact.getData()
        );
    }

    public Contact toContact() {
        return Contact.builder()
            .id(Contact.Id.parse(id))
            .shopId(Shop.Id.parse(shopId))
            .contactType(contactType)
            .data(data)
            .build();
    }
}