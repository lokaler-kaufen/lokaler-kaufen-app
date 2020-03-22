package de.qaware.mercury.mercury.storage.location.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface LocationDataRepository extends JpaRepository<GeoLocationEntity, String> {

    @Query("SELECT g FROM GeoLocationEntity AS g WHERE g.zipCode = :zipCode")
    List<GeoLocationEntity> searchZipCode(@Param("zipCode") String zipCode);

    @Query(
        "SELECT g " +
            "FROM GeoLocationEntity AS g WHERE (" +
            "      lower(g.zipCode) LIKE lower(:searchTerm)" +
            "   OR lower(g.placeName) LIKE lower(:searchTerm)" +
            ")"
    )
    List<GeoLocationEntity> search(@Param("searchTerm") String searchTerm);
}
