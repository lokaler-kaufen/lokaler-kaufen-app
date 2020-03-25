package de.qaware.mercury.business.i18n;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeI18nService {
    String formatDate(LocalDateTime dateTime);

    String formatDate(LocalDate date);

    String formatTime(LocalDateTime dateTime);

    String formatTime(LocalTime time);
}
