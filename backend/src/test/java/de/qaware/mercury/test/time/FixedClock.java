package de.qaware.mercury.test.time;

import de.qaware.mercury.business.time.Clock;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class FixedClock implements Clock {
    private final ZonedDateTime now;

    public FixedClock(ZonedDateTime now) {
        this.now = now;
    }

    @Override
    public ZonedDateTime nowZoned() {
        return now;
    }

    @Override
    public LocalDateTime now() {
        return now.toLocalDateTime();
    }
}
