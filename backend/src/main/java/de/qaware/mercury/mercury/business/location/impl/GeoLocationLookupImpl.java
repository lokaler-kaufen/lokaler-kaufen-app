package de.qaware.mercury.mercury.business.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;
import de.qaware.mercury.mercury.storage.location.LocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GeoLocationLookupImpl implements GeoLocationLookup {
    private final LocationRepository locationRepository;

    @Override
    @Transactional(readOnly = true)
    public GeoLocation fromZipCode(String zipCode) {
        return locationRepository.fromZipCode(zipCode);
    }

    @Override
    public List<GeoLocationSuggestion> search(String searchTerm) {
        return locationRepository.search(searchTerm);
    }
}
