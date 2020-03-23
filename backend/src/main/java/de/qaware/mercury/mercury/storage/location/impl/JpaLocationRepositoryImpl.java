package de.qaware.mercury.mercury.storage.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;
import de.qaware.mercury.mercury.storage.location.LocationRepository;
import de.qaware.mercury.mercury.util.Lists;
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
        return hits.isEmpty() ? null : hits.get(0).toGeoLocation();
    }

    @Override
    public List<GeoLocationSuggestion> search(String searchTerm) {
        List<GeoLocationEntity> hits = locationDataRepository.search(searchTerm + "%");
        log.debug("Found {} location hits for search term {}: {}", hits.size(), searchTerm, hits);
        return Lists.map(hits, GeoLocationEntity::toGeoLocationSuggestion);
    }
}
