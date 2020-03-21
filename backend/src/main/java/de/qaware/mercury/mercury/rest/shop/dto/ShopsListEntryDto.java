package de.qaware.mercury.mercury.rest.shop.dto;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class ShopsListEntryDto {
    String id;
    String name;
    double distance;
    List<String> supportedContactTypes;

    public static ShopsListEntryDto of(ShopListEntry shopListEntry) {
        return new ShopsListEntryDto(
            shopListEntry.getId().getId().toString(),
            shopListEntry.getName(),
            shopListEntry.getDistance(),
            Lists.map(shopListEntry.getContactTypes(), ContactType::getValue)
        );
    }
}
