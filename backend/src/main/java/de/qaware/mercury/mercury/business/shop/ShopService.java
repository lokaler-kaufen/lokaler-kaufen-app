package de.qaware.mercury.mercury.business.shop;

import de.qaware.mercury.mercury.storage.shop.ContactType;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopService {
    List<Shop> listAll();

    Shop create(String name, String ownerName, String email, String street, String zipCode, String city, String addressSupplement, List<ContactType> contactTypes);

    void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException;

    List<ShopWithDistance> findNearby(String location);

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
