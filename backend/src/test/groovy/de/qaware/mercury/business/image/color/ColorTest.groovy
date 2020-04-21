package de.qaware.mercury.business.image.color

import spock.lang.Specification

class ColorTest extends Specification {

    def "Cannot instantiate invalid color"(int red, int green, int blue) {
        when:
        Color.of(red, green, blue, "#FFFFFF");

        then:
        IllegalArgumentException _ = thrown()

        where:
        red | green | blue
        256 | 0     | 0
        -1  | 0     | 0
        0   | 256   | 0
        0   | -1    | 0
        0   | 0     | 256
        0   | 0     | -1
    }
}
