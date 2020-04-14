package de.qaware.mercury.business.location;

import de.qaware.mercury.business.location.impl.LocationNotFoundException;

import java.util.List;

public interface LocationService {
    /**
     * Looks up the location from the given zip code.
     *
     * @param zipCode zip code to search for
     * @return location
     * @throws LocationNotFoundException if the location isn't found
     */
    GeoLocation lookup(String zipCode) throws LocationNotFoundException;

    /**
     * Resolves the federal state from the given zip code.
     *
     * @param zipCode zip code to search for
     * @return federal state
     * @throws LocationNotFoundException if the location isn't found
     */
    FederalState resolveFederalState(String zipCode) throws LocationNotFoundException;

    /**
     * Search for all locations containing the given zip code.
     *
     * @param zipCode zip code to search for. Must have a length of at least 3, else an empty list will be returned.
     * @return all found hits
     */
    List<LocationSuggestion> suggest(String zipCode);

}
