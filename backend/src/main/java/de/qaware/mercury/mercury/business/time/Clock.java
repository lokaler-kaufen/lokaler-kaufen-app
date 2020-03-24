package de.qaware.mercury.mercury.business.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface Clock {
    ZonedDateTime nowZoned();

    default LocalDateTime now() {
        return nowZoned().toLocalDateTime();
    }

    default LocalDate today() {
        return now().toLocalDate();
    }
}
