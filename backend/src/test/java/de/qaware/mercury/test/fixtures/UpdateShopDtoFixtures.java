package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto;
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto;
import de.qaware.mercury.rest.shop.dto.requestresponse.BreaksDto;
import de.qaware.mercury.rest.shop.dto.requestresponse.SocialLinksDto;
import de.qaware.mercury.util.Maps;

public final class UpdateShopDtoFixtures {
    private UpdateShopDtoFixtures() {
    }

    public static UpdateShopDto create() {
        ShopUpdate shopUpdate = ShopUpdateFixtures.create();

        return new UpdateShopDto(
            shopUpdate.getName(), shopUpdate.getOwnerName(), shopUpdate.getStreet(), shopUpdate.getZipCode(),
            shopUpdate.getCity(), shopUpdate.getAddressSupplement(), shopUpdate.getDetails(), shopUpdate.getWebsite(),
            shopUpdate.isAutoColorEnabled(), Maps.mapKeys(shopUpdate.getContacts(), Enum::name),
            SlotConfigDto.of(shopUpdate.getSlotConfig()), SocialLinksDto.of(shopUpdate.getSocialLinks()),
            BreaksDto.of(Breaks.none())
        );
    }
}
