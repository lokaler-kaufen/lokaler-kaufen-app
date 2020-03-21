package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateDto {
    String id;
    String name;
    String ownerName;
    String street;
    String zipCode;
    String city;
    String addressSupplement;

    public static ShopCreateDto of(Shop shop) {
        return new ShopCreateDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement()
        );
    }
}
