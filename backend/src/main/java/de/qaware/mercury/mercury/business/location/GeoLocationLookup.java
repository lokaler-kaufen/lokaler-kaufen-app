package de.qaware.mercury.mercury.business.location;

import java.util.List;

public interface GeoLocationLookup {
    GeoLocation fromZipCode(String zipCode) throws LocationNotFoundException;

    /**
     * Search for zip code, city name.
     * Case insensitive prefix matching.
     *
     * @param searchTerm Term to search for. Must have a length of at least 3, else an empty list will be returned.
     * @return all found hits, or an empty list if the search term is not at least 3 chars long.
     */
    List<GeoLocationSuggestion> search(String searchTerm);
}
