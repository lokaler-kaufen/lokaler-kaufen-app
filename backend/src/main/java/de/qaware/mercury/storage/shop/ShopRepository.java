package de.qaware.mercury.storage.shop;

import de.qaware.mercury.business.location.BoundingBox;
import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ShopRepository {
    List<Shop> listAll();

    void insert(Shop shop);

    @Nullable
    Shop findById(Shop.Id id);

    List<Shop> findApproved();

    List<Shop> findApproved(BoundingBox searchArea);

    void update(Shop updatedShop);

    void deleteById(Shop.Id id);

    List<Shop> findByName(String name);

    List<Shop> search(String query);

    List<Shop> search(String query, BoundingBox searchArea);
}
