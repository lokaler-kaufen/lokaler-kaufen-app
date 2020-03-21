package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.Value;

@Value
public class ShopAdminDto {
    String id;
    String name;
    boolean enabled;

    public static ShopAdminDto of(Shop shop) {
        return new ShopAdminDto(
            shop.getId().getId().toString(),
            shop.getName(),
            shop.isEnabled()
        );
    }
}
