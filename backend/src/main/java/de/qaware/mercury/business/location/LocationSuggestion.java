package de.qaware.mercury.business.location;

import lombok.Value;

@Value
public class LocationSuggestion {
    String countryCode;
    String zipCode;
    String placeName;
}