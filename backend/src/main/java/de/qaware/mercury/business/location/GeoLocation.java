package de.qaware.mercury.business.location;

import lombok.Value;

@Value(staticConstructor = "of")
public class GeoLocation {
    double latitude;
    double longitude;
}