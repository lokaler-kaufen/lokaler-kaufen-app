package de.qaware.mercury.mercury.storage.shop;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopWithDistance;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);

    @Nullable
    Shop findById(Shop.Id id);

    List<ShopWithDistance> findNearby(double latitude, double longitude);

    void update(Shop updatedShop);

    void deleteById(Shop.Id id);
}
