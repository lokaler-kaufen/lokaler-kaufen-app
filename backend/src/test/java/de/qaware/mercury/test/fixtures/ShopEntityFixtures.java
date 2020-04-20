package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.shop.impl.ShopEntity;

import java.time.LocalTime;

public final class ShopEntityFixtures {
    private ShopEntityFixtures() {
    }

    public static ShopEntity create(UUIDFactory uuidFactory, Clock clock) {
        return new ShopEntity(uuidFactory.create(),
            "shop",
            "slug",
            "owner",
            "shop@local.host",
            "street",
            "zipCode",
            "city",
            "addressSupplement",
            "whatsapp",
            "phone",
            "faceTime",
            "googleDuo",
            "skype",
            "signal",
            "viber",
            true,
            true,
            null,
            0.0,
            0.0,
            "details",
            "website",
            15,
            5,
            60,
            LocalTime.of(7, 0),
            LocalTime.of(15, 0),
            LocalTime.of(8, 0),
            LocalTime.of(16, 0),
            LocalTime.of(10, 0),
            LocalTime.of(18, 0),
            LocalTime.of(11, 0),
            LocalTime.of(19, 0),
            LocalTime.of(12, 0),
            LocalTime.of(20, 0),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            null,
            null,
            "instagram",
            "facebook",
            "twitter",
            null,
            false,
            clock.nowZoned(),
            clock.nowZoned()
        );
    }
}
