package de.qaware.mercury.mercury.storage.shop;

import de.qaware.mercury.mercury.business.location.Location;
import de.qaware.mercury.mercury.business.shop.Shop;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);

    Shop findById(Shop.Id id);

    List<Shop> findNearby(double latitude, double longitude);
}
