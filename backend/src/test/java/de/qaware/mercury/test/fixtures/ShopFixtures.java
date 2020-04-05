package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.time.impl.WallClock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.test.uuid.TestUUIDFactory;
import org.springframework.lang.Nullable;

import java.time.LocalTime;
import java.util.Map;

public final class ShopFixtures {
    private ShopFixtures() {
    }

    public static Shop create() {
        return create(new TestUUIDFactory(), null, new WallClock());
    }

    public static Shop create(UUIDFactory uuidFactory, @Nullable Image.Id imageId, Clock clock) {
        return new Shop(
            Shop.Id.of(uuidFactory.create()), "name", "owner", "shop-1@local.host", "street", "81549", "München",
            "addressSupplement", Map.of(ContactType.WHATSAPP, "whatsapp"), true, true, imageId, GeoLocation.of(48.137154, 11.576124),
            "details", "http://local.host",
            new SlotConfig(15, 5,
                new DayConfig(LocalTime.of(7, 0), LocalTime.of(15, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(16, 0)),
                new DayConfig(LocalTime.of(10, 0), LocalTime.of(18, 0)),
                new DayConfig(LocalTime.of(11, 0), LocalTime.of(19, 0)),
                new DayConfig(LocalTime.of(12, 0), LocalTime.of(20, 0)),
                new DayConfig(LocalTime.of(10, 0), LocalTime.of(12, 0)),
                null
            ), clock.nowZoned(), clock.nowZoned()
        );
    }
}
