package de.qaware.mercury.mercury.storage.shop;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);

    @Nullable
    Shop findById(Shop.Id id);

    List<ShopListEntry> findNearby(double latitude, double longitude);

    void update(Shop updatedShop);

    void deleteById(Shop.Id id);
}
