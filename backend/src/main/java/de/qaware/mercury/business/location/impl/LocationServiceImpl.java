package de.qaware.mercury.business.location.impl;

import de.qaware.mercury.business.location.FederalState;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationService;
import de.qaware.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.storage.location.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
    private static final int DIGITS_IN_A_COMPLETE_ZIP_CODE = 5;
    private static final int MINIMUM_SUGGESTION_LENGTH = 1;
    static final int MAXIMUM_SUGGESTION_COUNT = 5;

    private final LocationRepository locationRepository;

    @Override
    @Transactional(readOnly = true)
    public GeoLocation lookup(String zipCode) throws LocationNotFoundException {
        GeoLocation geoLocation = locationRepository.fromZipCode(zipCode);
        if (geoLocation == null) {
            throw new LocationNotFoundException(zipCode);
        }

        return geoLocation;
    }

    @Override
    @Transactional(readOnly = true)
    public FederalState resolveFederalState(String zipCode) throws LocationNotFoundException {
        FederalState federalState = locationRepository.resolveFederalState(zipCode);
        if (federalState == null) {
            throw new LocationNotFoundException(zipCode);
        }

        return federalState;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationSuggestion> suggest(String zipCode) {
        if (zipCode.length() < MINIMUM_SUGGESTION_LENGTH) {
            return List.of();
        }

        // A complete zip code does not need partial matches, we just return whatever the DB finds
        if (zipCode.length() == DIGITS_IN_A_COMPLETE_ZIP_CODE) {
            return locationRepository.suggest(zipCode);
        }

        // If we just got a partial zip code, this is the general idea:
        // First, search for LIKE zipCode%
        // Then append to that list LIKE %zipCode%
        // Deduplicate all of them by zip code AND city (LocationKey), so we preserve different cities with the same zip code...

        // The LinkedHashMap will deduplicate them and preserve the order
        LinkedHashMap<LocationKey, LocationSuggestion> deduplicator = new LinkedHashMap<>(MAXIMUM_SUGGESTION_COUNT);

        // Search for matches which start with the zip code
        for (LocationSuggestion suggestion : locationRepository.suggest(zipCode + "%", MAXIMUM_SUGGESTION_COUNT)) {
            deduplicator.put(LocationKey.of(suggestion.getZipCode(), suggestion.getPlaceName()), suggestion);
        }
        // If we don't have enough, also search for matches which contain the zip code in them
        if (deduplicator.size() < MAXIMUM_SUGGESTION_COUNT) {
            // We need to get the max suggestion count again, cause we could get the same x elements as before and then end up with < 5 after deduplication
            for (LocationSuggestion suggestion : locationRepository.suggest("%" + zipCode + "%", MAXIMUM_SUGGESTION_COUNT)) {
                if (deduplicator.size() >= MAXIMUM_SUGGESTION_COUNT) {
                    break;
                }
                deduplicator.put(LocationKey.of(suggestion.getZipCode(), suggestion.getPlaceName()), suggestion);
            }
        }

        return new ArrayList<>(deduplicator.values());
    }

    @Value(staticConstructor = "of")
    static class LocationKey {
        String zipCode;
        String name;
    }
}
