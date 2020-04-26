package de.qaware.mercury.business.email;

import de.qaware.mercury.business.reservation.Reservation;

import java.time.LocalDateTime;

/**
 * Creates iCalendar files.
 */
public interface ICalendarService {
    /**
     * Creates an iCalendar file for a new reservation.
     *
     * @param reservationId id of the reservation
     * @param start         start of the reservation
     * @param end           end of the reservation
     * @param summary       iCalendar summary
     * @param description   iCalendar description
     * @return Content of the iCalendar file.
     */
    String newReservation(Reservation.Id reservationId, LocalDateTime start, LocalDateTime end, String summary, String description);
}
