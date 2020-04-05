package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopsAdminDto {
    List<ShopAdminDto> shops;

    public static ShopsAdminDto of(List<Shop> shops, ImageService imageService) {
        return new ShopsAdminDto(Lists.map(shops, shop -> ShopAdminDto.of(shop, imageService)));
    }
}
