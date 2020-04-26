package de.qaware.mercury.test.time;

import de.qaware.mercury.business.time.Clock;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
public class TestClock implements Clock {
    private ZonedDateTime now;

    public TestClock() {
        this(ZonedDateTime.now());
    }

    public TestClock(ZonedDateTime now) {
        this.now = now;
    }

    public void setNow(LocalDateTime now) {
        setNow(ZonedDateTime.of(now, ZoneId.of("Europe/Berlin")));
    }

    public void setNow(ZonedDateTime now) {
        log.info("It's now {}", now);
        this.now = now;
    }

    /**
     * Sets this clock to the current wall time.
     */
    public void setToWallTime() {
        setNow(ZonedDateTime.now());
    }

    @Override
    public ZonedDateTime nowZoned() {
        return now;
    }

    /**
     * Returns always the same datetime, '2020-04-26T18:51:52.387535+02:00[Europe/Berlin]'
     *
     * @return clock with a fixed datetime
     */
    public static TestClock fixed() {
        return new TestClock(ZonedDateTime.parse("2020-04-26T18:51:52.387535+02:00[Europe/Berlin]"));
    }
}
