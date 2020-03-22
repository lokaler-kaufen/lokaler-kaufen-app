package de.qaware.mercury.mercury.storage.location.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface LocationDataRepository extends JpaRepository<GeoLocationEntity, String> {

    @Query(
        "SELECT g as geoLocationEntity " +
            "FROM GeoLocationEntity AS g WHERE (g.zipCode LIKE lower(:zipCode))"
    )
    List<GeoLocationEntity> searchZipCode(@Param("zipCode") String zipCode);
}
