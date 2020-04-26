package de.qaware.mercury.business.email;

import de.qaware.mercury.business.reservation.Reservation;

public interface ICalendarService {
    String createICalendar(Reservation reservation, String summary, String description);
}
