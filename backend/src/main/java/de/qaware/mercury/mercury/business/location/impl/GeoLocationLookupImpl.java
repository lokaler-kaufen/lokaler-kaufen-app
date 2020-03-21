package de.qaware.mercury.mercury.business.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationLookupImpl implements GeoLocationLookup {
    @Override
    public GeoLocation fromZipCode(String zipCode) {
        double latitude = 48.104347;
        double longitude = 11.600918;

        // TODO: Look up PLZ to coordinates

        return new GeoLocation(latitude, longitude);
    }
}
