package de.qaware.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
    List<ShopEntity> findByName(String name);

    @Query("SELECT s FROM ShopEntity s JOIN FETCH s.contactTypes2")
    List<ShopEntity> findAllWithContactTypes();

    @Query(
        "SELECT s as shopEntity, " +
            "sqrt(" +
            "  power(111.3 * cos((:latitude + s.latitude) / (2 * 0.01745)) * (:longitude - s.longitude) , 2)" +
            " + power(111.3 * (:latitude - s.latitude), 2)" +
            ") as distance FROM ShopEntity AS s " +
            "WHERE s.enabled = true AND s.approved = true"
    )
    List<ShopWithDistanceProjection> findNearby(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query(
        "SELECT s as shopEntity, " +
            "sqrt(" +
            "  power(111.3 * cos((:latitude + s.latitude) / (2 * 0.01745)) * (:longitude - s.longitude) , 2)" +
            " + power(111.3 * (:latitude - s.latitude), 2)" +
            ") as distance FROM ShopEntity AS s " +
            "WHERE (lower(s.name) LIKE lower(:query) OR lower(s.details) LIKE lower(:query)) AND s.enabled = true AND s.approved = true"
    )
    List<ShopWithDistanceProjection> search(@Param("query") String query, @Param("latitude") double latitude, @Param("longitude") double longitude);
}
