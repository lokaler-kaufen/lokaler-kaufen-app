package de.qaware.mercury.business.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface Clock {
    ZonedDateTime nowZoned();

    LocalDateTime now();

    default LocalDate today() {
        return now().toLocalDate();
    }
}
