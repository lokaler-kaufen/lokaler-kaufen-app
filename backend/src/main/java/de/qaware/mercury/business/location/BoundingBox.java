package de.qaware.mercury.business.location;

import lombok.Value;

@Value(staticConstructor = "of")
public class BoundingBox {
    GeoLocation southWest;
    GeoLocation northEast;
}
