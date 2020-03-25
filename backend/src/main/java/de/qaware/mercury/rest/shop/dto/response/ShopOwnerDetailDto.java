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
    private String website;
    private Map<String, String> contactTypes;
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
            shop.getWebsite(),
            Maps.mapKeys(shop.getContactTypes(), ContactType::toString),
            Null.map(shop.getSlotConfig(), SlotConfigDto::of)
        );
    }
}
