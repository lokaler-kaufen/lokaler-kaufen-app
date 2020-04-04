package de.qaware.mercury.image;

import de.qaware.mercury.business.TechnicalException;

public class ImageException extends TechnicalException {
    public ImageException(String message) {
        super(message);
    }

    public ImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
