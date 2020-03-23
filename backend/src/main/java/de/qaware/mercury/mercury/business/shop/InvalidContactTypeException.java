package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.TechnicalException;

public class InvalidContactTypeException extends TechnicalException {
    public InvalidContactTypeException(String input, Throwable cause) {
        super(String.format("Invalid contact type '%s'", input), cause);
    }
}
