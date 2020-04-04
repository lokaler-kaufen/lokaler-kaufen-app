package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto;
import de.qaware.mercury.util.Maps;
import de.qaware.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopOwnerDetailDto {
    private String id;
    private String name;
    private String ownerName;
    private String street;
    private String zipCode;
    private String city;
    private String addressSupplement;
    private String details;
    private String imageId;
    private String website;
    private Map<String, String> contacts;
    private SlotConfigDto slots;

    public static ShopOwnerDetailDto of(Shop shop) {
        return new ShopOwnerDetailDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            shop.getDetails(),
            Null.map(shop.getImageId(), id -> id.getId().toString()),
            shop.getWebsite(),
            Maps.mapKeys(shop.getContacts(), ContactType::getId),
            Null.map(shop.getSlotConfig(), SlotConfigDto::of)
        );
    }
}
