package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.BusinessException;

public class ShopNotFoundException extends BusinessException {
    public ShopNotFoundException(Shop.Id shopId) {
        super(String.format("Shop with id %s not found", shopId));
    }
}
