package de.qaware.mercury.mercury.business.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Clock {
    LocalDateTime now();

    default LocalDate today() {
        return now().toLocalDate();
    }
}
