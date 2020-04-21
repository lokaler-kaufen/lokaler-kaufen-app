package de.qaware.mercury.business.image.color

import spock.lang.Specification

class ColorMapTest extends Specification {

    def "Color map resizes correctly"() {
        given:
        ColorMap colorMap = new ColorMap(10, 10)

        when:
        colorMap.setColor(8, 9, Color.of(0, 0, 0, "#FFFFFF"))

        then:
        colorMap.getWidth() == 9
        colorMap.getHeight() == 10
    }

    def "Invalid colors cannot be set"(int x, int y) {
        given:
        ColorMap colorMap = new ColorMap(10, 10)

        when:
        colorMap.setColor(x, y, Color.of(255, 255, 255, "#FFFFFF"))

        then:
        IllegalArgumentException _ = thrown()

        where:
        x  | y
        0  | 10
        0  | -1
        10 | 0
    }

    def "Invalid colors cannot be gotten"(int x, int y) {
        given:
        ColorMap colorMap = new ColorMap(10, 10)
        colorMap.setColor(5, 6, Color.of(255, 255, 255, "#FFFFFF"))

        when:
        colorMap.getColor(x, y)

        then:
        IllegalArgumentException _ = thrown()

        where:
        x  | y
        0  | 7
        6  | 0
        -1 | 0
        0  | -1
    }

}
