package de.qaware.mercury.business.image.color.analysis.impl

import de.qaware.mercury.business.image.ImageException
import de.qaware.mercury.business.image.color.Color
import de.qaware.mercury.business.image.color.analysis.DominantColorFinder
import spock.lang.Specification

class DominantColorFinderImplTest extends Specification {
    DominantColorFinder dominantColorFinder = new DominantColorFinderImpl();

    def "test findDominantColor"() {
        expect:
        dominantColorFinder.findDominantColor(colorHistogram) == color
        where:
        colorHistogram                                                                 | color
        Map.of(Color.of(255, 0, 0, "#FF0000"), 3, Color.of(0, 255, 255, "#00FF00"), 2) | Color.of(255, 0, 0, "#FF0000")
        Map.of(Color.of(0, 255, 255, "#00FFFF"), 2)                                    | Color.of(0, 255, 255, "#00FFFF")
    }

    def "empty map throws exception"() {
        when:
        dominantColorFinder.findDominantColor(Map.of())

        then:
        ImageException _ = thrown()
    }
}
