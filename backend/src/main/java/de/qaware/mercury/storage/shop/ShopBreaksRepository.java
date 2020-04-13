package de.qaware.mercury.storage.shop;

import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

public interface ShopBreaksRepository {
    void insert(Shop.Id shopId, Breaks breaks);

    void update(Shop.Id shopId, Breaks breaks);

    @Nullable
    Breaks findByShopId(Shop.Id shopId);
}
