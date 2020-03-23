package de.qaware.mercury.mercury.storage.location;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;

import java.util.List;

public interface LocationRepository {
    GeoLocation fromZipCode(String zipCode);

    /**
     * Search for zip code, city name.
     * Case insensitive substring matching.
     *
     * @param searchTerm Term to search for. Must have a length of at least 3, else an empty list will be returned.
     * @return all found hits, or an empty list if the search term is not at least 3 chars long.
     */
    List<GeoLocationSuggestion> search(String searchTerm);
}
