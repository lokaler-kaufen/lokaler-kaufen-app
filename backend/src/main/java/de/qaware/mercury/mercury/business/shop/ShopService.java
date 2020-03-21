package de.qaware.mercury.mercury.business.shop;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name, String street, String zipCode, String city);

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<ShopWithDistance> findNearby(String location);
}
