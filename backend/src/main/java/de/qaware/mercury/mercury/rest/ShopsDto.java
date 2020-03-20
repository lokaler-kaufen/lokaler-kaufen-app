package de.qaware.mercury.mercury.rest;

import de.qaware.mercury.mercury.business.Shop;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
class ShopsDto {
    List<ShopDto> shops;

    public static ShopsDto of(List<Shop> shops) {
        return new ShopsDto(Lists.map(shops, s -> ShopDto.of(s)));
    }
}
