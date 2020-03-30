package de.qaware.mercury.business.location;

import lombok.Value;

/**
 * A data class for geographical locations.
 */
@Value(staticConstructor = "of")
public class GeoLocation {
    double latitude;
    double longitude;
}