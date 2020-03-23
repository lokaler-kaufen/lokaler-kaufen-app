package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.TechnicalException;

public class InvalidShopIdException extends TechnicalException {
    public InvalidShopIdException(String id, Throwable cause) {
        super(String.format("Invalid shop id: '%s'", id), cause);
    }
}
