package de.qaware.mercury.test.fixtures;

import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.time.impl.WallClock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.test.uuid.TestUUIDFactory;

import java.util.UUID;

public final class ReservationFixture {
    private ReservationFixture() {
    }

    public static Reservation create() {
        return create(Shop.Id.of(UUID.randomUUID()));
    }

    public static Reservation create(Shop.Id shopId) {
        return create(new TestUUIDFactory(), new WallClock(), shopId);
    }

    public static Reservation create(UUIDFactory uuidFactory, Clock clock, Shop.Id shopId) {
        return new Reservation(
            Reservation.Id.random(uuidFactory), shopId, clock.now(), clock.now(), "contact", "reservation@local.host",
            "name", ContactType.WHATSAPP, false, clock.nowZoned(), clock.nowZoned()
        );
    }
}
