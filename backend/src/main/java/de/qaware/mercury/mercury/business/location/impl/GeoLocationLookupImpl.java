package de.qaware.mercury.mercury.business.location.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationLookupImpl implements GeoLocationLookup {
    @Override
    public GeoLocation fromZipCode(String zipCode) {
        switch (zipCode) {
            case "85579":
                return new GeoLocation(48.08140, 11.63480);
            case "81549":
                return new GeoLocation(48.137154, 11.576124);
            case "55116":
                return new GeoLocation(49.98419, 8.2791);
            default:
                return new GeoLocation(48.137154, 11.576124);
        }
    }
}
