package de.qaware.mercury.business.image.color

import spock.lang.Specification
import spock.lang.Unroll

class ColorDistanceUtilTest extends Specification {

    @Unroll
    def "Check color distance"() {
        expect:
        ColorDistanceUtil.getColorDistance(color1, color2) >= lower_bound
        if (upper_bound) ColorDistanceUtil.getColorDistance(color1, color2) <= upper_bound

        where:
        color1                         | color2                           | lower_bound | upper_bound
        // same color
        Color.of(255, 0, 0, "#FF0000") | Color.of(255, 0, 0, "#FF0000")   | 0.0         | 0.01
        // very similar color
        Color.of(255, 0, 0, "#FE0000") | Color.of(254, 0, 0, "#FE0000")   | 0.0         | 0.5
        // different color
        Color.of(255, 0, 0, "#FE0000") | Color.of(0, 255, 255, "#00FF00") | 5.0         | null
    }
}
