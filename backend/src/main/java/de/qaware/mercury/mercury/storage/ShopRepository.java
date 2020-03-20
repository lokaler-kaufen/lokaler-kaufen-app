package de.qaware.mercury.mercury.storage;

import de.qaware.mercury.mercury.business.Shop;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();
}
