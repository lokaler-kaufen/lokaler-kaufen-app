package de.qaware.mercury.business.image.color.analysis.impl

import de.qaware.mercury.business.image.color.Color
import de.qaware.mercury.business.image.color.analysis.ColorConfidenceCalculator
import spock.lang.Specification
import spock.lang.Subject

class ColorConfidenceCalculatorImplTest extends Specification {

    @Subject
    ColorConfidenceCalculator colorConfidenceCalculator = new ColorConfidenceCalculatorImpl();

    def "test calculateConfidence"(Color color, Map<Color, Integer> colorHistogram, double lower_bound, double upper_bound) {

        expect:
        colorConfidenceCalculator.calculateConfidence(color, colorHistogram) >= lower_bound
        colorConfidenceCalculator.calculateConfidence(color, colorHistogram) <= upper_bound

        where:
        color                          | colorHistogram                                                               | lower_bound | upper_bound
        // the histogram has only one color
        Color.of(255, 0, 0, "#FF0000") | Map.of(Color.of(255, 0, 0, "#FF0000"), 5)                                    | 0.999       | 1.00
        // 80% red / 20% green
        Color.of(255, 0, 0, "#FF0000") | Map.of(Color.of(255, 0, 0, "#FF0000"), 4, Color.of(0, 255, 0, "#00FF00"), 1) | 0.799       | 0.801
        // 0% red / 100% green
        Color.of(255, 0, 0, "#FF0000") | Map.of(Color.of(255, 0, 0, "#FF0000"), 0, Color.of(0, 255, 0, "#00FF00"), 1) | 0.000       | 0.001
        // 100% very similar red / 0% green
        Color.of(255, 0, 0, "#FF0000") | Map.of(Color.of(254, 0, 0, "#FE0000"), 1)                                    | 0.999       | 1.00
    }
}
