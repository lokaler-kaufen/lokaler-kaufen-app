package de.qaware.mercury.mercury.storage.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.storage.location.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaLocationRepositoryImpl implements LocationRepository {
    private final LocationDataRepository locationDataRepository;

    @Override
    public GeoLocation fromZipCode(String zipCode) {
        List<GeoLocationEntity> hits = locationDataRepository.searchZipCode(zipCode);

        if (!hits.isEmpty()) {
            log.debug("Found {} hits for zipcode {}: {}", hits.size(), zipCode, hits);
            return hits.get(0).toGeoLocation();
        } else {
            // default location in case the zip code does not exist
            return GeoLocation.QAWARE_M;
        }
    }
}
