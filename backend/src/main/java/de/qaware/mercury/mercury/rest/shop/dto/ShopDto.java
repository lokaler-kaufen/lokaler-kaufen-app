package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.Value;

@Value
public class ShopDto {
    String id;
    String name;

    public static ShopDto of(Shop shop) {
        return new ShopDto(
            shop.getId().getId().toString(),
            shop.getName()
        );
    }
}
