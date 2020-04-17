package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.SocialLinks;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.time.impl.WallClock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.test.builder.SlotConfigBuilder;
import de.qaware.mercury.test.uuid.TestUUIDFactory;
import org.springframework.lang.Nullable;

import java.time.LocalTime;
import java.util.Map;

public final class ShopFixtures {
    private ShopFixtures() {
    }

    public static Shop create() {
        return create(new TestUUIDFactory(), ImageIdFixture.create(), new WallClock());
    }

    public static Shop create(Image.Id imageId) {
        return create(new TestUUIDFactory(), imageId, new WallClock());
    }

    public static Shop create(UUIDFactory uuidFactory, @Nullable Image.Id imageId, Clock clock) {
        return new Shop(
            Shop.Id.of(uuidFactory.create()), "name", "slug", "owner", "shop-1@local.host", "street", "81549", "München",
            "addressSupplement", Map.of(ContactType.WHATSAPP, "whatsapp"), true, true, imageId, "", false, GeoLocation.of(48.137154, 11.576124),
            "details", "http://local.host",
            new SlotConfigBuilder().setTimePerSlot(15).setTimeBetweenSlots(5).setMonday(new DayConfig(LocalTime.of(7, 0), LocalTime.of(15, 0))).setTuesday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(16, 0))).setWednesday(new DayConfig(LocalTime.of(10, 0), LocalTime.of(18, 0))).setThursday(new DayConfig(LocalTime.of(11, 0), LocalTime.of(19, 0))).setFriday(new DayConfig(LocalTime.of(12, 0), LocalTime.of(20, 0))).setSaturday(new DayConfig(LocalTime.of(10, 0), LocalTime.of(12, 0))).setSunday(null).build(),
            new SocialLinks("instagram", "facebook", "twitter"),
            clock.nowZoned(), clock.nowZoned()
        );
    }
}
