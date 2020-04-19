package de.qaware.mercury.business.shop;

import de.qaware.mercury.business.BusinessException;

public class ShopNotFoundException extends BusinessException {
    private ShopNotFoundException(String message) {
        super(message);
    }

    public static ShopNotFoundException ofShopId(Shop.Id shopId) {
        return new ShopNotFoundException(String.format("Shop with id %s not found", shopId));
    }

    public static ShopNotFoundException ofSlug(String slug) {
        return new ShopNotFoundException(String.format("Shop with slug '%s' not found", slug));
    }
}
