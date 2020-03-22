package de.qaware.mercury.mercury.storage.location;

import de.qaware.mercury.mercury.business.location.GeoLocation;

public interface LocationRepository {
    GeoLocation fromZipCode(String zipCode);
}
