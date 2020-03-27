package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.TechnicalException;

public class InvalidContactTypeException extends TechnicalException {
    public InvalidContactTypeException(String input) {
        super(String.format("Invalid contact type '%s'", input));
    }
}
