package de.qaware.mercury.mercury.business.shop;

import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name, String ownerName, String street, String zipCode, String city, String addressSupplement);

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<ShopWithDistance> findNearby(String location);

    void delete(Shop.Id parse) throws ShopNotFoundException;

    @Nullable
    Shop findById(Shop.Id id);
}
