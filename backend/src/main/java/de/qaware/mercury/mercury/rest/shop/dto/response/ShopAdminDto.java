package de.qaware.mercury.mercury.rest.shop.dto.response;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopAdminDto {
    String id;
    String name;
    String ownerName;
    String email;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    Set<ContactType> contactTypes;
    boolean enabled;
    String details;
    @Nullable
    String website;

    public static ShopAdminDto of(Shop shop) {
        return new ShopAdminDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            shop.getContactTypes().keySet(),
            shop.isEnabled(),
            shop.getDetails(),
            shop.getWebsite()
        );
    }
}
