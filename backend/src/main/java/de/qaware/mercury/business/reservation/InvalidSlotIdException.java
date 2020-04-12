package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.TechnicalException;

public class InvalidSlotIdException extends TechnicalException {
    public InvalidSlotIdException(String input) {
        this(input, null);
    }

    public InvalidSlotIdException(String input, Throwable cause) {
        super(String.format("Invalid slot id '%s'", input), cause);
    }
}
