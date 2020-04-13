package de.qaware.mercury.storage.shop;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.Shop;

public interface ShopBreaksRepository {
    void insert(Shop.Id shopId, Breaks breaks);
}
