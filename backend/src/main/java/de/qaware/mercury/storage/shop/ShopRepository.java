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

    /**
     * Finds all approved and enabled shops
     *
     * @return all approved and enabled shops
     */
    List<Shop> findActive();

    /**
     * Finds all approved and enabled shops within the given search area
     *
     * @param searchArea the search area
     * @return all approved and enabled shops within the given search area
     */
    List<Shop> findActive(BoundingBox searchArea);

    void update(Shop updatedShop);

    void deleteById(Shop.Id id);

    List<Shop> findByName(String name);

    /**
     * Searches approved and enabled shops matching the given query
     *
     * @param query the search query, supporting '%' as a 'like' operator
     * @return all approved and enabled shops matching the given query
     */
    List<Shop> searchActive(String query);

    /**
     * Searches approved and enabled shops matching the given query within the given search area
     *
     * @param query      the search query, supporting '%' as a 'like' operator
     * @param searchArea the search area
     * @return all approved and enabled shops matching the given query within the given search area
     */
    List<Shop> searchActive(String query, BoundingBox searchArea);

    @Nullable
    Shop findBySlug(String slug);
}
