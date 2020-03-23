package de.qaware.mercury.mercury.business.location;

import lombok.Value;

@Value
public class GeoLocation {
    double latitude;
    double longitude;

    public double distanceTo(GeoLocation other) {
        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
        double KM_PER_NAUTICAL_MILE = 1.852216;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon2 = Math.toRadians(other.longitude);

        // great circle distance in radians, using law of cosines formula
        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        // each degree on a great circle of Earth is 60 nautical miles
        double nauticalMiles = 60 * Math.toDegrees(angle);
        return KM_PER_NAUTICAL_MILE * nauticalMiles;
    }
}