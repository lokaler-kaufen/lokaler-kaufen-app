package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.Contact;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto;
import de.qaware.mercury.util.Maps;
import de.qaware.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Map;

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
    Map<ContactType, String> contactTypes;
    boolean enabled;
    boolean approved;
    String details;
    @Nullable
    String website;
    SlotConfigDto slots;

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
            Maps.mapList(shop.getContacts(), Contact::getContactType, Contact::getData),
            shop.isEnabled(),
            shop.isApproved(),
            shop.getDetails(),
            shop.getWebsite(),
            Null.map(shop.getSlotConfig(), SlotConfigDto::of)
        );
    }
}
