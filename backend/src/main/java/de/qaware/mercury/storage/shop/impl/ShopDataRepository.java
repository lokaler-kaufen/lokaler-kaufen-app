package de.qaware.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
    List<ShopEntity> findByName(String name);

    /**
     * Finds all approved and enabled shops
     *
     * @return all approved and enabled shops
     */
    @Query(
        "SELECT s FROM ShopEntity s WHERE s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> findActive();

    /*
     *
     * This query will not work, if the minimum longitude is in the eastern hemisphere (>0) while the maximum longitude
     * is in the western hemisphere (<0). Those regions in question are mostly ocean and do not have any zipCodes,
     * so we're fine with this simpler solution for the moment. If we want to support the eastern most regions of Russia,
     * we need to add another query which handles the case maxLong < minLong correctly.
     *
     */

    /**
     * Finds all approved and enabled shops within the given coordinates matching the given query string
     *
     * @param query        a query string, supporting '%' as a 'like' operator
     * @param maxLatitude  the maximum latitude (must be > minimum latitude)
     * @param maxLongitude the maximum longitude (must be > minimum longitude)
     * @param minLatitude  the minimum latitude (must be < minimum latitude)
     * @param minLongitude the minimum longitude (must be < maximum longitude)
     * @return all shop entities matching the given query within the given coordinates
     */
    @Query(
        "SELECT s FROM ShopEntity s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true " +
            "AND s.latitude <= :maxLatitude AND s.latitude >= :minLatitude AND s.longitude <= :maxLongitude AND s.longitude >= :minLongitude"
    )
    List<ShopEntity> searchActive(@Param("query") String query,
                                  @Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                                  @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    /*
     *
     * This query will not work, if the minimum longitude is in the eastern hemisphere (>0) while the maximum longitude
     * is in the western hemisphere (<0). Those regions in question are mostly ocean and do not have any zipCodes,
     * so we're fine with this simpler solution for the moment. If we want to support the eastern most regions of Russia,
     * we need to add another query which handles the case maxLong < minLong correctly.
     *
     */

    /**
     * Finds all approved and enabled shops within the given coordinates
     *
     * @param maxLatitude  the maximum latitude (must be > minimum latitude)
     * @param maxLongitude the maximum longitude (must be > minimum longitude)
     * @param minLatitude  the minimum latitude (must be < minimum latitude)
     * @param minLongitude the minimum longitude (must be < maximum longitude)
     * @return all approved (and enabled) shops within the given coordinates
     */
    @Query(
        "SELECT s FROM ShopEntity s WHERE s.enabled = true AND s.approved = true " +
            "AND s.latitude <= :maxLatitude AND s.latitude >= :minLatitude AND s.longitude <= :maxLongitude AND s.longitude >= :minLongitude"
    )
    List<ShopEntity> findActive(@Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                                @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    /**
     * Finds all approved and enabled shops matching the given query string
     *
     * @param query a query string, supporting '%' as a 'like' operator
     * @return all shop entities matching the given query
     */
    @Query(
        "SELECT s FROM ShopEntity s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> searchActive(@Param("query") String query);

    @Nullable
    ShopEntity findBySlug(String slug);
}
