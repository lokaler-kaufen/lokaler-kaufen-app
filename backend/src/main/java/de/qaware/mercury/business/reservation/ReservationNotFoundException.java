package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.BusinessException;

public class ReservationNotFoundException extends BusinessException {
    public ReservationNotFoundException(Reservation.Id id) {
        this(id, null);
    }

    public ReservationNotFoundException(Reservation.Id id, Throwable cause) {
        super(String.format("Reservation with id '%s' not found", id), cause);
    }
}
