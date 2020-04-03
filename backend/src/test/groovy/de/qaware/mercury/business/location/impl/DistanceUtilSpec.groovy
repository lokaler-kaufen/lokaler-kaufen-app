package de.qaware.mercury.business.location.impl

import de.qaware.mercury.business.location.BoundingBox
import de.qaware.mercury.business.location.GeoLocation
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

class DistanceUtilSpec extends Specification {
    def "distance between Grassau and Munich"() {
        given:
        GeoLocation grassau = GeoLocation.of(47.779400d, 12.451130d);
        GeoLocation munich = GeoLocation.of(48.104346d, 11.600851d);

        when:
        double dist = DistanceUtil.distanceInKmBetween(grassau, munich);

        then:
        dist > 73
    }

    def "boundingbox"() {
        given:
        // we take a fixed geolocation and check if the corners of the box are, where they're supposed to be.
        GeoLocation schwedenstein = GeoLocation.of(48.091751d, 11.678344d);
        double distance = 5d;

        when:
        // distance to a corner of a 10x10 box from its middle is sqrt(5^2 + 5^2) = 7,07 [a^2 + b^2 = c^2]
        // this means the bottom left and top right corner are about 7,07 km apart from the middle of the box
        // ne 48.13670 | 11.74552
        BoundingBox boundingBox = DistanceUtil.boundingBoxOf(schwedenstein, distance);

        then:
        // it's enough if we are somewhere close to those 7.07km, let's give it 100 meters tolerance in both directions along the diagonal
        // according to google maps, these are the coordinates for
        // ~7,17km sw 48.045864 | 11.609941
        // ~6,97km sw 48.047062 | 11.611872
        assertThat(boundingBox.getSouthWest().getLatitude()).isBetween(48.045864d, 48.047062d);
        assertThat(boundingBox.getSouthWest().getLongitude()).isBetween(11.609941d, 11.611872d);

        // ~7,17km ne 48.137211 | 11.746262
        // ~6,97km ne 48.135926 | 11.744367
        assertThat(boundingBox.getNorthEast().getLatitude()).isBetween(48.135926d, 48.137211d);
        assertThat(boundingBox.getNorthEast().getLongitude()).isBetween(11.744367d, 11.746262d);
    }
}
