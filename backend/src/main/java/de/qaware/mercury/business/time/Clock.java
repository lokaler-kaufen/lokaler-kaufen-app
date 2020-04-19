package de.qaware.mercury.business.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public interface Clock {
    ZonedDateTime nowZoned();

    default LocalDateTime now() {
        return nowZoned().toLocalDateTime();
    }

    default LocalDate today() {
        return nowZoned().toLocalDate();
    }

    default Date nowAsLegacyDate() {
        return Date.from(nowZoned().toInstant());
    }
}
