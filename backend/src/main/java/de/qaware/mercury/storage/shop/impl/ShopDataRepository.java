package de.qaware.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
    List<ShopEntity> findByName(String name);

    @Query(
        "SELECT s as shopEntity  FROM ShopEntity AS s WHERE s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> findNearby();

    @Query(
        "SELECT s as shopEntity FROM ShopEntity AS s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true " +
            "AND s.latitude < :maxLatitude AND s.latitude > :minLatitude AND s.longitude < :maxLongitude AND s.longitude > :minLongitude"
    )
    List<ShopEntity> search(@Param("query") String query,
                            @Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                            @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    @Query(
        "SELECT s as shopEntity  FROM ShopEntity AS s WHERE s.enabled = true AND s.approved = true " +
            "AND s.latitude < :maxLatitude AND s.latitude > :minLatitude AND s.longitude < :maxLongitude AND s.longitude > :minLongitude"
    )
    List<ShopEntity> findNearby(@Param("maxLatitude") double maxLatitude, @Param("maxLongitude") double maxLongitude,
                                @Param("minLatitude") double minLatitude, @Param("minLongitude") double minLongitude);

    @Query(
        "SELECT s as shopEntity FROM ShopEntity AS s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true"
    )
    List<ShopEntity> search(@Param("query") String query);
}
