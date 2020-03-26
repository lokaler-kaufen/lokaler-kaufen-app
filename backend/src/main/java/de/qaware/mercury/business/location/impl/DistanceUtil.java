package de.qaware.mercury.business.location.impl;

import de.qaware.mercury.business.location.GeoLocation;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * Helpers for distance calculation
 */
public final class DistanceUtil {
    private static final Ellipsoid ELLIPSOID = Ellipsoid.WGS84;
    private static final GeodeticCalculator CALCULATOR = new GeodeticCalculator();
    private static final double METERS_IN_1_KM = 1000.0;

    private DistanceUtil() {
    }

    /**
     * Calculates the distance based on the 2D geodetic curve between the two coordinates.
     *
     * @param p1 first geo coordinate
     * @param p2 second geo coordinate
     * @return distance between the two coordinates in kilometers
     */
    public static double distanceInKmBetween(GeoLocation p1, GeoLocation p2) {
        GlobalCoordinates cord1 = new GlobalCoordinates(p1.getLatitude(), p1.getLongitude());
        GlobalCoordinates cord2 = new GlobalCoordinates(p2.getLatitude(), p2.getLongitude());

        GeodeticCurve geoCurve = CALCULATOR.calculateGeodeticCurve(ELLIPSOID, cord1, cord2);
        return geoCurve.getEllipsoidalDistance() / METERS_IN_1_KM;
    }
}