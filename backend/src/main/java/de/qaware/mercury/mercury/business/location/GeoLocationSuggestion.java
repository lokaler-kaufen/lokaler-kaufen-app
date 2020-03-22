package de.qaware.mercury.mercury.business.location;

import lombok.Value;

@Value
public class GeoLocationSuggestion {
    String countryCode;
    String zipCode;
    String placeName;
    String adminName1;
    String adminName2;
    String adminName3;
}