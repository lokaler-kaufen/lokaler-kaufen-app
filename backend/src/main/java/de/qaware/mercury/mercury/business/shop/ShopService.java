package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.business.location.LocationNotFoundException;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(ShopCreation creation) throws LocationNotFoundException;

    Shop update(Shop shop, ShopUpdate update) throws LocationNotFoundException;

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<ShopWithDistance> findNearby(String zipCode) throws LocationNotFoundException;

    void delete(Shop.Id parse) throws ShopNotFoundException;

    @Nullable
    Shop findById(Shop.Id id);

    Shop findByIdOrThrow(Shop.Id id) throws ShopNotFoundException;

    /**
     * Sends the email with the creation link for a new shop to the given email address
     *
     * @param email email address
     */
    void sendCreateLink(String email);

    List<Shop> findByName(String name);

    List<ShopWithDistance> search(String query, String zipCode) throws LocationNotFoundException;
}
