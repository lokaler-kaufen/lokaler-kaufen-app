package de.qaware.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
    List<ShopEntity> findByName(String name);

    /**
     * Finds all approved (and enabled) shops
     *
     * @return all approved (and enabled) shops
     */
    @Query(
        "SELECT s as shopEntity  FROM ShopEntity AS s WHERE s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> findApproved();

    /**
     * Finds all shops within the given coordinates matching the given query string
     *
     * @param query        a query string, supporting '%' as a 'like' operator
     * @param maxLatitude  the maximum latitude
     * @param maxLongitude the maximum longitude
     * @param minLatitude  the minimum latitude
     * @param minLongitude the minimum longitude
     * @return all shop entities matching the given query within the given coordinates
     */
    @Query(
        "SELECT s as shopEntity FROM ShopEntity AS s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true " +
            "AND s.latitude < :maxLatitude AND s.latitude > :minLatitude AND s.longitude < :maxLongitude AND s.longitude > :minLongitude"
    )
    List<ShopEntity> search(@Param("query") String query,
                            @Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                            @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    /**
     * Finds all approved (and enabled) shops within the given coordinates
     *
     * @param maxLatitude  the maximum latitude
     * @param maxLongitude the maximum longitude
     * @param minLatitude  the minimum latitude
     * @param minLongitude the minimum longitude
     * @return all approved (and enabled) shops within the given coordinates
     */
    @Query(
        "SELECT s as shopEntity  FROM ShopEntity AS s WHERE s.enabled = true AND s.approved = true " +
            "AND s.latitude < :maxLatitude AND s.latitude > :minLatitude AND s.longitude < :maxLongitude AND s.longitude > :minLongitude"
    )
    List<ShopEntity> findApproved(@Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                                  @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    /**
     * Finds all shops matching the given query string
     *
     * @param query a query string, supporting '%' as a 'like' operator
     * @return all shop entities matching the given query
     */
    @Query(
        "SELECT s as shopEntity FROM ShopEntity AS s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> search(@Param("query") String query);
}
