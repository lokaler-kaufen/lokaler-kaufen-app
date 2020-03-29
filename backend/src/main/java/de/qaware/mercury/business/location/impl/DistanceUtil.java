package de.qaware.mercury.business.location.impl;

import de.qaware.mercury.business.location.BoundingBox;
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

    /**
     * Calculates a box around a given coordinate with each side being exactly the given distance away from this coordinate.
     *
     * @param p        the geo coordinate
     * @param distance the minimum distance from p to each side of the box in kilometers
     * @return a box with each side being distance away from p
     */
    public static BoundingBox boundingBoxOf(GeoLocation p, double distance) {
        Ellipsoid reference = Ellipsoid.WGS84;
        double hypotenuse = Math.sqrt(distance * 1000 * distance * 1000 * 2);
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
        GlobalCoordinates geoLocationCoordinates = new GlobalCoordinates(p.getLatitude(), p.getLongitude());
        GlobalCoordinates southWest = geodeticCalculator.calculateEndingGlobalCoordinates(reference, geoLocationCoordinates, 225, hypotenuse);
        GlobalCoordinates northEast = geodeticCalculator.calculateEndingGlobalCoordinates(reference, geoLocationCoordinates, 45, hypotenuse);
        return BoundingBox.of(GeoLocation.of(southWest.getLatitude(), southWest.getLongitude()), GeoLocation.of(northEast.getLatitude(), northEast.getLongitude()));
    }
}