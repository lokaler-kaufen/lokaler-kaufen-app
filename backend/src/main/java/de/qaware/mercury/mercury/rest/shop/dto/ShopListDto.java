package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopListDto {
    List<ShopListEntryDto> shops;

    public static ShopListDto of(List<ShopListEntry> shops) {
        return new ShopListDto(Lists.map(shops, ShopListEntryDto::of));
    }

    @Value
    public static class ShopListEntryDto {
        String id;
        String name;
        double distance;
        List<String> supportedContactTypes;

        public static ShopListEntryDto of(ShopListEntry shopListEntry) {
            return new ShopListEntryDto(
                shopListEntry.getId().getId().toString(),
                shopListEntry.getName(),
                shopListEntry.getDistance(),
                Lists.map(shopListEntry.getContactTypes(), ContactType::getValue)
            );
        }
    }
}
