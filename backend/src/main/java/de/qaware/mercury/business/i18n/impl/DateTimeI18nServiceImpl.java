package de.qaware.mercury.business.i18n.impl;

import de.qaware.mercury.business.i18n.DateTimeI18nService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
class DateTimeI18nServiceImpl implements DateTimeI18nService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String formatDate(LocalDateTime dateTime) {
        return formatDate(dateTime.toLocalDate());
    }

    @Override
    public String formatDate(LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    @Override
    public String formatTime(LocalDateTime dateTime) {
        return formatTime(dateTime.toLocalTime());
    }

    @Override
    public String formatTime(LocalTime time) {
        return TIME_FORMATTER.format(time);
    }
}
