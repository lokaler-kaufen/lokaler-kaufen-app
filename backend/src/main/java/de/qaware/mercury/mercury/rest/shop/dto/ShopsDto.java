package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopsDto {
    List<ShopDto> shops;

    public static ShopsDto of(List<Shop> shops) {
        return new ShopsDto(Lists.map(shops, ShopDto::of));
    }
}
