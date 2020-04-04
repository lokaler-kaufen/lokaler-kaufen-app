package de.qaware.mercury.storage.image;

import de.qaware.mercury.business.TechnicalException;

public class ImageStorageException extends TechnicalException {
    public ImageStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
