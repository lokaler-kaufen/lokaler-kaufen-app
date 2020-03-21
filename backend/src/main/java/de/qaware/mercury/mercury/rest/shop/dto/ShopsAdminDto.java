package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopsAdminDto {
    List<ShopAdminDto> shops;

    public static ShopsAdminDto of(List<Shop> shops) {
        return new ShopsAdminDto(Lists.map(shops, ShopAdminDto::of));
    }
}
