package de.qaware.mercury.business.image.color.analysis;

import de.qaware.mercury.business.image.color.Color;

import java.util.Map;

/**
 * Calculates the confidence that a given color is the dominant color in a given histogram.
 */
public interface ColorConfidenceCalculator {

    /**
     * Calculates the confidence that a given color is the dominant color in a given histogram.
     *
     * @param color          the color in question
     * @param colorHistogram a histogram of all colors to compare the color to (key: color,
     *                       value: absolute occurrence of the color)
     * @return A double value ranging from 0.0 to 1.0; the higher the value, the greater the confidence.
     */
    double calculateConfidence(Color color, Map<Color, Integer> colorHistogram);
}
