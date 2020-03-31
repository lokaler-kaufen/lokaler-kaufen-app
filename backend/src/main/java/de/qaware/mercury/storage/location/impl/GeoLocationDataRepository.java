package de.qaware.mercury.storage.location.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface GeoLocationDataRepository extends JpaRepository<GeoLocationEntity, String> {

    /**
     * Finds all known geographical locations for a given zip code.
     *
     * @param zipCode  the zip code to look up
     * @param pageable a pageable which can also be used to limit results
     * @return a list of geographical locations with the given zip code
     */
    @Query("SELECT g FROM GeoLocationEntity g WHERE g.zipCode = :zipCode")
    List<GeoLocationEntity> findByZipCode(@Param("zipCode") String zipCode, Pageable pageable);

    /**
     * Finds all known geographical locations for a given zip code search expression.
     *
     * @param zipCode  the zip code to look up, may contain '%' as a 'LIKE' operator
     * @param pageable a pageable which can also be used to limit results
     * @return a list of geographical locations matching the given zip code search expression.
     */
    @Query("SELECT g FROM GeoLocationEntity g WHERE g.zipCode LIKE :zipCode ORDER BY g.zipCode")
    List<GeoLocationEntity> findByZipCodeLike(@Param("zipCode") String zipCode, Pageable pageable);
}
