package de.qaware.mercury.storage.location.impl;

import de.qaware.mercury.business.location.FederalState;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.storage.location.LocationRepository;
import de.qaware.mercury.util.Lists;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCode(zipCode, PageRequest.of(0, 1));
        if (hits.isEmpty()) {
            return null;
        }

        // Always use first hit
        return hits.get(0).toGeoLocation();
    }

    @Override
    public List<LocationSuggestion> suggest(String zipCode, int maxResults) {
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCodeLike(zipCode, PageRequest.of(0, maxResults));
        return Lists.map(hits, GeoLocationEntity::toLocationSuggestion);
    }

    @Override
    public List<LocationSuggestion> suggest(String zipCode) {
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCodeLike(zipCode, Pageable.unpaged());
        return Lists.map(hits, GeoLocationEntity::toLocationSuggestion);
    }

    @Override
    @Nullable
    public FederalState resolveFederalState(String zipCode) {
        List<GeoLocationEntity> hits = geoLocationDataRepository.findByZipCode(zipCode, PageRequest.of(0, 1));
        if (hits.isEmpty()) {
            return null;
        }

        // Always use first hit
        return FederalState.parse(hits.get(0).getStateShort());
    }
}
