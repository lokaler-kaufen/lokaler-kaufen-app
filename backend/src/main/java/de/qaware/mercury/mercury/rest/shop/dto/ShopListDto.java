package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopListDto {
    List<ShopsListEntryDto> shops;

    public static ShopListDto of(List<ShopListEntry> shops) {
        return new ShopListDto(Lists.map(shops, ShopsListEntryDto::of));
    }
}
