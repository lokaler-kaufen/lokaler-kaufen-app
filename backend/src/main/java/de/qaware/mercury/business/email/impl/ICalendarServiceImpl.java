package de.qaware.mercury.business.email.impl;

import de.qaware.mercury.business.email.ICalendarService;
import de.qaware.mercury.business.reservation.Reservation;
import de.qaware.mercury.business.time.Clock;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ICalendarServiceImpl implements ICalendarService {
    private static final String NEWLINE = "\r\n"; // Must be \r\n to adhere the standard
    private static final DateTimeFormatter UTC = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"); // 19970714T170000Z
    private static final DateTimeFormatter LOCAL = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"); // 19970714T170000

    private final Clock clock;

    @Override
    public String createICalendar(Reservation reservation, String summary, String description) {
        return "BEGIN:VCALENDAR" + NEWLINE +
            "VERSION:2.0" + NEWLINE +
            "PRODID:-//lokaler.kaufen/lokaler.kaufen//EN" + NEWLINE +
            "BEGIN:VEVENT" + NEWLINE +
            "UID:" + reservation.getId().getId() + NEWLINE +
            "ORGANIZER;CN=lokaler.kaufen:MAILTO:info@lokaler.kaufen" + NEWLINE + // TODO: Replace with properties
            "DTSTAMP:" + formatDateUTC(clock.nowZoned()) + NEWLINE +
            "DTSTART:" + formatDateLocal(reservation.getStart()) + NEWLINE +
            "DTEND:" + formatDateLocal(reservation.getStart()) + NEWLINE +
            "SUMMARY:" + escapeText(summary) + NEWLINE +
            "DESCRIPTION:" + escapeText(description) + NEWLINE +
            "END:VEVENT" + NEWLINE +
            "END:VCALENDAR" + NEWLINE;
    }

    // See https://www.kanzaki.com/docs/ical/dateTime.html
    private String formatDateLocal(LocalDateTime dateTime) {
        return dateTime.format(LOCAL);
    }

    // See https://www.kanzaki.com/docs/ical/dateTime.html
    private String formatDateUTC(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneOffset.UTC).format(UTC);
    }

    // See https://www.kanzaki.com/docs/ical/text.html
    private String escapeText(String text) {
        return text
            .replace("\\", "\\\\") // \ -> \\
            .replace("\r\n", "\\n") // newline -> \n
            .replace("\r", "\\n") // newline -> \n
            .replace("\n", "\\n") // newline -> \n
            .replace(";", "\\;") // ; -> \;
            .replace(",", "\\,"); // ; -> \;
    }
}
