package de.qaware.mercury.rest.shop.dto.response;

import de.qaware.mercury.business.shop.Contact;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Sets;
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
        Set<ContactType> supportedContactTypes;

        public static ShopListEntryDto of(ShopWithDistance shopListEntry) {
            return new ShopListEntryDto(
                shopListEntry.getShop().getId().getId().toString(),
                shopListEntry.getShop().getName(),
                shopListEntry.getDistance(),
                Sets.map(shopListEntry.getShop().getContacts(), Contact::getContactType)
            );
        }
    }
}
