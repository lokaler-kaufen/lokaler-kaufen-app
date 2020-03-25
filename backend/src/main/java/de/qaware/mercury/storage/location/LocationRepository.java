package de.qaware.mercury.storage.location;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationSuggestion;
import org.springframework.lang.Nullable;

import java.util.List;

public interface LocationRepository {
    @Nullable
    GeoLocation fromZipCode(String zipCode);

    /**
     * Suggests locations based on the given (partial) zipCode.
     *
     * @param zipCode    the zipCode (can be 'liked' by '%')
     * @param maxResults the maximum number of results
     * @return a list of location suggestions (at most maxResults entries)
     */
    List<LocationSuggestion> suggest(String zipCode, int maxResults);
}
