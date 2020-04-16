package de.qaware.mercury.business.image.impl

import de.qaware.mercury.business.image.ColorFindResult
import de.qaware.mercury.business.image.Image
import spock.lang.Specification

class ColorFinderImplTest extends Specification {
    def "test getDominantColor"() {
        given:
        InputStream testImage = getClass().getClassLoader().getResourceAsStream("test/images/1.jpg");
        ColorFinderImpl sut = new ColorFinderImpl();
        when:
        ColorFindResult output = sut.getDominantColor(new Image.Id(UUID.randomUUID()), testImage);

        then:
        output.getHexColor() == "#123456"
        output.confidence > 0.99 && output.confidence < 1.01
    }
}
