package de.qaware.mercury.business.image.impl

import de.qaware.mercury.business.image.ColorFindResult
import de.qaware.mercury.business.image.Image
import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

class ColorFinderImplTest extends IntegrationTestSpecification {

    @Subject
    @Autowired
    ColorFinderImpl sut;

    def "test getDominantColor"() {
        given:
        // a 128x128 picture with the uniform color #008081
        InputStream testImage = getClass().getClassLoader().getResourceAsStream("test/images/2.jpg");
        when:
        ColorFindResult output = sut.getDominantColor(new Image.Id(UUID.randomUUID()), testImage);

        then:
        output.getHexColor() == "#008081"
        output.confidence > 0.99 && output.confidence < 1.01
    }
}
