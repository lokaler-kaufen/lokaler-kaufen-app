package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.location.Location;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name, Location location, boolean enabled);

    Shop create(String name, String postCode, boolean enabled);

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<Shop> findNearby(String location);
}
