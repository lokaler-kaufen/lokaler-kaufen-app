package de.qaware.mercury.business.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public interface Clock {
    ZonedDateTime nowZoned();

    LocalDateTime now();

    default LocalDate today() {
        return now().toLocalDate();
    }

    default Date nowAsLegacyDate() {
        return Date.from(nowZoned().toInstant());
    }
}
