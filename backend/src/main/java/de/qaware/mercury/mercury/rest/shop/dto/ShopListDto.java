package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.mercury.util.Lists;
import de.qaware.mercury.mercury.util.Sets;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class ShopListDto {
    List<ShopListEntryDto> shops;

    public static ShopListDto of(List<ShopWithDistance> shops) {
        return new ShopListDto(Lists.map(shops, ShopListEntryDto::of));
    }

    @Value
    public static class ShopListEntryDto {
        String id;
        String name;
        double distance;
        Set<String> supportedContactTypes;

        public static ShopListEntryDto of(ShopWithDistance shopListEntry) {
            return new ShopListEntryDto(
                shopListEntry.getShop().getId().getId().toString(),
                shopListEntry.getShop().getName(),
                shopListEntry.getDistance(),
                Sets.map(shopListEntry.getShop().getContactTypes().keySet(), ContactType::name)
            );
        }
    }
}
