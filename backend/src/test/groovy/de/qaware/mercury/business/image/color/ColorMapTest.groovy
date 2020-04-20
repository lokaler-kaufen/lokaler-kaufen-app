package de.qaware.mercury.business.image.color

import spock.lang.Specification

class ColorMapTest extends Specification {

    def "Color map resizes correctly"() {
        given:
        ColorMap colorMap = new ColorMap(10, 10)

        when:
        colorMap.setColor(4, 5, Color.of(0, 0, 0, "#FFFFFF"))

        then:
        colorMap.getWidth() == 5
        colorMap.getHeight() == 6

    }

}
