package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.TechnicalException;

public class InvalidTimeException extends TechnicalException {
    public InvalidTimeException(String input, Throwable cause) {
        super(String.format("Invalid time: '%s'", input), cause);
    }
}
