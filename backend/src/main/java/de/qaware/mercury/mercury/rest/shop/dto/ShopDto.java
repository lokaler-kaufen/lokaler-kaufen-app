package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.ShopWithDistance;
import lombok.Value;

@Value
public class ShopDto {
    String id;
    String name;
    boolean enabled;
    double distance;

    public static ShopDto of(ShopWithDistance shopWithDistance) {
        return new ShopDto(
            shopWithDistance.getShop().getId().getId().toString(),
            shopWithDistance.getShop().getName(),
            shopWithDistance.getShop().isEnabled(),
            shopWithDistance.getDistance()
        );
    }
}
