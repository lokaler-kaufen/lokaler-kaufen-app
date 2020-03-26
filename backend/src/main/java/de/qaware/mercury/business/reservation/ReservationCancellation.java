package de.qaware.mercury.business.reservation;

import lombok.Value;

@Value
public class ReservationCancellation {
    Reservation.Id reservationId;
    ReservationCancellationSide side;
}
