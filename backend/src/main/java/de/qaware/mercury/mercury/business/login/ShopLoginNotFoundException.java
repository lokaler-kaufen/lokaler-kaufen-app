package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.BusinessException;

public class ShopLoginNotFoundException extends BusinessException {
    public ShopLoginNotFoundException(String email) {
        super(String.format("Shop login with email '%s' not found", email));
    }
}
