package de.qaware.mercury.mercury.business.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.LocationService;
import de.qaware.mercury.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.mercury.storage.location.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LocationServiceImpl implements LocationService {
    // Munich
    static final GeoLocation DEFAULT_GEOLOCATION = GeoLocation.of(48.104346, 11.600851);
    private static final int MINIMUM_SUGGESTION_LENGTH = 3;

    private final LocationRepository locationRepository;

    @Override
    @Transactional(readOnly = true)
    public GeoLocation lookup(String zipCode) {
        GeoLocation geoLocation = locationRepository.fromZipCode(zipCode);
        if (geoLocation == null) {
            return DEFAULT_GEOLOCATION;
        }

        return geoLocation;
    }

    @Override
    public List<LocationSuggestion> suggest(String zipCode) {
        if (zipCode.length() < MINIMUM_SUGGESTION_LENGTH) {
            return List.of();
        }

        // General idea
        // First, search for LIKE zipCode%
        // Then append to that list LIKE %zipCode%
        // Then deduplicate that list by zip code

        // Don't remove that new ArrayList() stuff, this way we ensure that addAll doesn't throw exceptions if the returned list is immutable!
        List<LocationSuggestion> suggestions = new ArrayList<>(locationRepository.suggest(zipCode + "%"));
        suggestions.addAll(locationRepository.suggest("%" + zipCode + "%"));

        // The LinkedHashMap will deduplicate them and preserve the order
        LinkedHashMap<String, LocationSuggestion> deduplicator = new LinkedHashMap<>(suggestions.size());
        for (LocationSuggestion suggestion : suggestions) {
            deduplicator.put(suggestion.getZipCode(), suggestion);
        }

        return new ArrayList<>(deduplicator.values());
    }
}
