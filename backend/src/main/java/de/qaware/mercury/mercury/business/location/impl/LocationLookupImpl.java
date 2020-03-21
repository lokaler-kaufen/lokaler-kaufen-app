package de.qaware.mercury.mercury.business.location.impl;

import de.qaware.mercury.mercury.business.location.Location;
import de.qaware.mercury.mercury.business.location.LocationLookup;
import org.springframework.stereotype.Service;

@Service
public class LocationLookupImpl implements LocationLookup {
    @Override
    public Location fromPostcode(String postCode) {
        String name = "Dummy";
        double latitude = 48.104347;
        double longitude = 11.600918;

        // TODO: Look up PLZ to coordinates

        return new Location(name, latitude, longitude);
    }
}
