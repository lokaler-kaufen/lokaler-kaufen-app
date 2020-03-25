package de.qaware.mercury.storage.location.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface GeoLocationDataRepository extends JpaRepository<GeoLocationEntity, String> {

    @Query("SELECT g FROM GeoLocationEntity g WHERE g.zipCode = :zipCode")
    List<GeoLocationEntity> findByZipCode(@Param("zipCode") String zipCode);

    @Query("SELECT g FROM GeoLocationEntity g WHERE g.zipCode LIKE :zipCode ORDER BY g.zipCode")
    List<GeoLocationEntity> findByZipCodeLike(@Param("zipCode") String zipCode, Pageable pageable);
}
