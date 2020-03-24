package de.qaware.mercury.mercury.storage.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.mercury.storage.location.LocationRepository;
import de.qaware.mercury.mercury.util.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaLocationRepositoryImpl implements LocationRepository {
    private final GeoLocationDataRepository geoLocationDataRepository;

    @Override
    @Nullable
    public GeoLocation fromZipCode(String zipCode) {
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCode(zipCode);
        if (hits.isEmpty()) {
            return null;
        }

        // Always use first hit
        return hits.get(0).toGeoLocation();
    }

    @Override
    public List<LocationSuggestion> suggest(String zipCode) {
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCodeLike(zipCode);
        return Lists.map(hits, GeoLocationEntity::toLocationSuggestion);
    }
}
