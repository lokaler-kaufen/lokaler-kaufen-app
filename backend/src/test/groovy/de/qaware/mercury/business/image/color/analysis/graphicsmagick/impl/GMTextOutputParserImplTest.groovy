package de.qaware.mercury.business.image.color.analysis.graphicsmagick.impl

import de.qaware.mercury.business.image.ImageException
import de.qaware.mercury.business.image.color.Color
import de.qaware.mercury.business.image.color.ColorMap
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMTextOutputParser
import spock.lang.Specification
import spock.lang.Subject

class GMTextOutputParserImplTest extends Specification {
    @Subject
    GMTextOutputParser gmTextOutputParser = new GMTextOutputParserImpl();

    def "test parseGMTextOutput"() {
        given:
        String exampleOutput =
            """0,0: ( 78,169, 54) #4EA936
              |1,0: ( 78,172, 53) #4EAC35
              |2,0: ( 78,172, 53) #4EAC35
              |0,1: ( 87,175, 64) #57AF40
              |1,1: ( 86,174, 62) #56AE3E
              |2,1: ( 86,174, 62) #56AE3E""".stripMargin()

        when:
        ColorMap result = gmTextOutputParser.parseGMTextOutput(exampleOutput)

        then:
        result.width == 3
        result.height == 2
        result.getColor(0, 0) == Color.of(78, 169, 54, "#4EA936")
        result.getColor(1, 0) == Color.of(78, 172, 53, "#4EAC35")
        result.getColor(2, 0) == Color.of(78, 172, 53, "#4EAC35")
        result.getColor(0, 1) == Color.of(87, 175, 64, "#57AF40")
        result.getColor(1, 1) == Color.of(86, 174, 62, "#56AE3E")
        result.getColor(2, 1) == Color.of(86, 174, 62, "#56AE3E")
    }

    def "invalid input throws an exception"() {
        given:
        String exampleOutput =
            """0,0: ( 78,169, 54) #4EA936
              |1,0: ( 78,172, 53) #4EAC35
              |garbage input
              |2,0: ( 78,172, 53) #4EAC35
              |0,1: ( 87,175, 64) #57AF40
              |1,1: ( 86,174, 62) #56AE3E
              |2,1: ( 86,174, 62) #56AE3E""".stripMargin()

        when:
        gmTextOutputParser.parseGMTextOutput(exampleOutput)

        then:
        ImageException _ = thrown()
    }
}
