package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.Arrays;
import java.util.UUID;

/**
 * Used as a data holder in the SqlResultSetMapping on {@link ShopEntity}.
 */
@Value
class ShopWithDistance {
    UUID id;
    String name;
    double distance;
    String[] contactTypes;

    public ShopListEntry toShopListEntry() {
        return new ShopListEntry(Shop.Id.of(id), name, distance, Lists.map(Arrays.asList(contactTypes), ContactType::valueOf));
    }
}
