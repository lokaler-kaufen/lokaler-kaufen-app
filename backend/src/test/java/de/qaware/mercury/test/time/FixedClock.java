package de.qaware.mercury.test.time;

import de.qaware.mercury.business.time.Clock;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

public class FixedClock implements Clock {
    @Getter
    @Setter
    private ZonedDateTime now;

    public FixedClock(ZonedDateTime now) {
        this.now = now;
    }

    @Override
    public ZonedDateTime nowZoned() {
        return now;
    }
}
