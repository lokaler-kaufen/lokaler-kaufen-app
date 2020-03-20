package de.qaware.mercury.mercury.storage.shop;

import de.qaware.mercury.mercury.business.shop.Shop;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);
}
