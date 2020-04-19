package de.qaware.mercury.business.image.color.analysis.impl;

import de.qaware.mercury.business.image.color.Color;
import de.qaware.mercury.business.image.color.ColorDistanceUtil;
import de.qaware.mercury.business.image.color.analysis.ColorConfidenceCalculator;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * A simple implementation of the color confidence calculator which uses the perceived
 * color distance Delta E to identify all very similar colors in the histogram and return
 * the ratio of these colors to all colors in the histogram.
 * <p>
 * This means that the ratio will be 1.0 (or 100%) if the identified color is indistinguishable
 * from the rest of the histogram and 0.0 (or 0%) if the identified color is different from all
 * colors in the given color histogram.
 */
@Service
public class ColorConfidenceCalculatorImpl implements ColorConfidenceCalculator {

    // hardly distinguishable: https://de.wikipedia.org/wiki/Delta_E#Bewertung_von_%CE%94E
    private static final double COLOR_DISTANCE_THRESHOLD = 1.0;

    @Override
    public double calculateConfidence(Color color, Map<Color, Integer> colorHistogram) {
        double similarColors = colorHistogram.get(color);
        for (Color otherColor : colorHistogram.keySet()) {
            if (color.equals(otherColor)) {
                continue;
            }
            if (ColorDistanceUtil.getColorDistance(color, otherColor) < COLOR_DISTANCE_THRESHOLD) {
                similarColors += colorHistogram.get(otherColor);
            }
        }
        return similarColors / colorHistogram.values().stream().reduce(Integer::sum).orElseThrow();
    }

}
