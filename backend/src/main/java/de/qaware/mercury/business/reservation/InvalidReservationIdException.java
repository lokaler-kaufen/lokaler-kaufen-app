package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.TechnicalException;

public class InvalidReservationIdException extends TechnicalException {
    public InvalidReservationIdException(String id, Throwable cause) {
        super(String.format("Invalid reservation id: '%s'", id), cause);
    }
}
