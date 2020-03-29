package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.BusinessException;

public class ReservationFailedException extends BusinessException {
    public ReservationFailedException(String message) {
        super(message);
    }
}
