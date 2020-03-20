package de.qaware.mercury.mercury.rest;

import de.qaware.mercury.mercury.business.Shop;
import lombok.Value;

@Value
class ShopDto {
    String id;
    String name;

    public static ShopDto of(Shop shop) {
        return new ShopDto(
            shop.getId().getId().toString(),
            shop.getName()
        );
    }
}
