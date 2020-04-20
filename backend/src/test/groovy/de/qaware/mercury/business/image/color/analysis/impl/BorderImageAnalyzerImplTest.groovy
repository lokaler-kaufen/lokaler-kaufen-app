package de.qaware.mercury.business.image.color.analysis.impl

import de.qaware.mercury.business.image.color.Color
import de.qaware.mercury.business.image.color.ColorMap
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMImageToTextConverter
import de.qaware.mercury.business.image.color.analysis.graphicsmagick.GMTextOutputParser
import spock.lang.Specification

class BorderImageAnalyzerImplTest extends Specification {

    def "test createImageHistogram"() {
        given:
        String mockImage =
            """0,0: ( 78,169, 54) #4EA936
              |1,0: ( 78,172, 53) #4EAC35
              |2,0: ( 78,172, 53) #4EAC35
              |0,1: ( 87,175, 64) #57AF40
              |1,1: ( 86,174, 62) #56AE3E
              |2,1: ( 86,174, 62) #56AE3E""".stripMargin()

        ColorMap mockMap = new ColorMap(3, 3)
        mockMap.setColor(0, 0, Color.of(78, 169, 54, "#4EA936"))
        mockMap.setColor(1, 0, Color.of(78, 172, 53, "#4EAC35"))
        mockMap.setColor(2, 0, Color.of(78, 172, 53, "#4EAC35"))
        mockMap.setColor(0, 1, Color.of(87, 175, 64, "#57AF40"))
        mockMap.setColor(1, 1, Color.of(86, 174, 62, "#56AE3E"))
        mockMap.setColor(2, 1, Color.of(86, 174, 62, "#56AE3E"))

        GMImageToTextConverter imageToTextConverter = Mock()
        _ * imageToTextConverter.convertImageToText(_, _) >> mockImage
        GMTextOutputParser textOutputParser = Mock()
        _ * textOutputParser.parseGMTextOutput(mockImage) >> mockMap

        BorderImageAnalyzerImpl borderImageAnalyzer = new BorderImageAnalyzerImpl(imageToTextConverter, textOutputParser)

        when:
        Map<Color, Integer> result = borderImageAnalyzer.createImageHistogram(null, 3)

        then:
        result.size() == 4
        result.get(Color.of(78, 169, 54, "#4EA936")) == 1
        result.get(Color.of(78, 172, 53, "#4EAC35")) == 2
        result.get(Color.of(87, 175, 64, "#57AF40")) == 1
        result.get(Color.of(86, 174, 62, "#56AE3E")) == 2
    }
}
