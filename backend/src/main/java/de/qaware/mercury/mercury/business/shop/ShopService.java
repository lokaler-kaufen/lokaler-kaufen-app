package de.qaware.mercury.mercury.business.shop;

import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(ShopCreation creation);

    Shop update(Shop shop, ShopUpdate update);

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<ShopListEntry> findNearby(String location);

    void delete(Shop.Id parse) throws ShopNotFoundException;

    @Nullable
    Shop findById(Shop.Id id);

    /**
     * Sends the email with the creation link for a new shop to the given email address
     *
     * @param email email address
     */
    void sendCreateLink(String email);
}
