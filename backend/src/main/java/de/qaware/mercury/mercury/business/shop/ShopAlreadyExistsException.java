package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.BusinessException;

public class ShopAlreadyExistsException extends BusinessException {
    public ShopAlreadyExistsException(String email) {
        super(String.format("A shop with the email address '%s' already exists", email));
    }
}
