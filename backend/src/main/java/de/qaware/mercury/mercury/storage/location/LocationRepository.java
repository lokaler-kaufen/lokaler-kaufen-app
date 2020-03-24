package de.qaware.mercury.mercury.storage.location;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.LocationSuggestion;
import org.springframework.lang.Nullable;

import java.util.List;

public interface LocationRepository {
    @Nullable
    GeoLocation fromZipCode(String zipCode);

    List<LocationSuggestion> suggest(String zipCode);
}
