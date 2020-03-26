package de.qaware.mercury.business.location.impl;

import de.qaware.mercury.business.location.GeoLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DistanceUtilTest {

    @Test
    public void testDistances(){
        GeoLocation grassau = GeoLocation.of(47.779400, 12.451130);
        GeoLocation munich = GeoLocation.of(48.104346, 11.600851);
        double dist = DistanceUtil.distanceInKmBetween(grassau, munich);
        assertTrue(dist > 73);
        System.out.println(dist);
    }
}
