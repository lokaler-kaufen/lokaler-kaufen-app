package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopContact;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDetailDto {
    String id;
    String name;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Set<ContactType> contactTypes;
    String details;
    @Nullable
    String website;

    public static ShopDetailDto of(Shop shop) {
        return new ShopDetailDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            Sets.map(shop.getContacts(), ShopContact::getContactType),
            shop.getDetails(),
            shop.getWebsite()
        );
    }
}
