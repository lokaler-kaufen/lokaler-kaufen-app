package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.SlotConfig;

import java.time.LocalTime;
import java.util.Map;

public final class ShopCreationFixtures {
    private ShopCreationFixtures() {
    }

    public static ShopCreation create() {
        return new ShopCreation(
            "shop-1@local.host", "owner", "name", "street", "81549", "München", "addressSupplement", "details", "website",
            "shop-1", Map.of(ContactType.WHATSAPP, "whatsapp"), new SlotConfig(15, 5,
            new DayConfig(LocalTime.of(7, 0), LocalTime.of(15, 0)),
            new DayConfig(LocalTime.of(8, 0), LocalTime.of(16, 0)),
            new DayConfig(LocalTime.of(10, 0), LocalTime.of(18, 0)),
            new DayConfig(LocalTime.of(11, 0), LocalTime.of(19, 0)),
            new DayConfig(LocalTime.of(12, 0), LocalTime.of(20, 0)),
            new DayConfig(LocalTime.of(10, 0), LocalTime.of(12, 0)),
            null
        ));
    }
}
