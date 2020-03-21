package de.qaware.mercury.mercury.business.location;

public interface GeoLocationLookup {
    GeoLocation fromZipCode(String zipCode);
}
