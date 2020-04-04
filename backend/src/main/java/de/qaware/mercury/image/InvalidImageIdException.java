package de.qaware.mercury.image;

import de.qaware.mercury.business.TechnicalException;

public class InvalidImageIdException extends TechnicalException {
    public InvalidImageIdException(String id, Throwable cause) {
        super(String.format("Invalid image id: '%s'", id), cause);
    }
}
