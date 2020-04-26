package de.qaware.mercury.business.email;

import de.qaware.mercury.business.reservation.Reservation;

import java.time.LocalDateTime;

public interface ICalendarService {
    String createICalendar(Reservation.Id reservationId, LocalDateTime start, LocalDateTime end, String summary, String description);
}
