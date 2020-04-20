package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.business.shop.SocialLinks;
import de.qaware.mercury.test.builder.SlotConfigBuilder;

import java.time.LocalTime;
import java.util.Map;

public final class ShopUpdateFixtures {
    private ShopUpdateFixtures() {
    }

    public static ShopUpdate create() {
        return new ShopUpdate(
            "name", "owner", "street", "81549", "München", "addressSupplement", "details", "https://local.host", false,
            Map.of(ContactType.WHATSAPP, "whatsapp"),
            new SlotConfigBuilder().setTimePerSlot(15).setTimeBetweenSlots(5).setMonday(new DayConfig(LocalTime.of(7, 0), LocalTime.of(15, 0))).setTuesday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(16, 0))).setWednesday(new DayConfig(LocalTime.of(10, 0), LocalTime.of(18, 0))).setThursday(new DayConfig(LocalTime.of(11, 0), LocalTime.of(19, 0))).setFriday(new DayConfig(LocalTime.of(12, 0), LocalTime.of(20, 0))).setSaturday(new DayConfig(LocalTime.of(10, 0), LocalTime.of(12, 0))).setSunday(null).build(),
            new SocialLinks("instagram", "facebook", "twitter"), Breaks.none()
        );
    }
}
