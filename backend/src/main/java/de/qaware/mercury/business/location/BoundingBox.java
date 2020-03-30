package de.qaware.mercury.business.location;

import lombok.Value;

/**
 * A data class describing a rectangular box by its south-western and north-eastern corner
 */
@Value(staticConstructor = "of")
public class BoundingBox {
    GeoLocation southWest;
    GeoLocation northEast;
}
