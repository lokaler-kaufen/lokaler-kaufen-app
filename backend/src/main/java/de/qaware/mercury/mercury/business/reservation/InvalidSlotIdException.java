package de.qaware.mercury.mercury.business.reservation;

import de.qaware.mercury.mercury.business.TechnicalException;

public class InvalidSlotIdException extends TechnicalException {
    public InvalidSlotIdException(String input, Throwable cause) {
        super(String.format("Invalid slot id '%s'", input), cause);
    }
}
