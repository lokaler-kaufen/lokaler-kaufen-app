package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.TechnicalException;

public class InvalidBreakException extends TechnicalException {
    public InvalidBreakException(String start, String end, String message, Throwable cause) {
        super(String.format("Break (start = '%s', end = '%s') is invalid: %s", start, end, message), cause);
    }

    public InvalidBreakException(String message) {
        super(message);
    }
}
