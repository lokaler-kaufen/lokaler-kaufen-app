package de.qaware.mercury.storage.shop;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopWithDistance;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);

    @Nullable
    Shop findById(Shop.Id id);

    List<ShopWithDistance> findNearby(GeoLocation location);

    void update(Shop updatedShop);

    void deleteById(Shop.Id id);

    List<Shop> findByName(String name);

    List<ShopWithDistance> search(String query, GeoLocation location);
}
