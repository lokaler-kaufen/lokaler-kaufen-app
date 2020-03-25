package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopsAdminDto {
    List<ShopAdminDto> shops;

    public static ShopsAdminDto of(List<Shop> shops) {
        return new ShopsAdminDto(Lists.map(shops, ShopAdminDto::of));
    }
}
