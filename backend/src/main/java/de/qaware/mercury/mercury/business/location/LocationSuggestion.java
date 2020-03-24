package de.qaware.mercury.mercury.business.location;

import lombok.Value;

@Value
public class LocationSuggestion {
    String countryCode;
    String zipCode;
    String placeName;
}