package de.qaware.mercury.business.location;

import de.qaware.mercury.business.TechnicalException;

public class InvalidFederalStateException extends TechnicalException {
    public InvalidFederalStateException(String input) {
        super(String.format("Invalid federal state '%s'", input));
    }
}
