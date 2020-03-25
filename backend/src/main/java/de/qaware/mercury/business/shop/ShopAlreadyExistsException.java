package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.BusinessException;

public class ShopAlreadyExistsException extends BusinessException {
    public ShopAlreadyExistsException(String email) {
        super(String.format("A shop with the email address '%s' already exists", email));
    }
}
