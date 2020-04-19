package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.util.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopsAdminDto {
    private List<ShopAdminDto> shops;

    public static ShopsAdminDto of(List<Shop> shops, ShopService shopService) {
        return new ShopsAdminDto(Lists.map(shops, shop -> ShopAdminDto.of(shop, shopService)));
    }
}
