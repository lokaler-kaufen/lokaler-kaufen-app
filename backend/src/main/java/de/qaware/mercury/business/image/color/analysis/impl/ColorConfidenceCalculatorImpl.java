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
 * <p>
 * Be aware that this is a simple but not 100% correct way to calculate the confidence.
 * If the histogram contains 100% of a very similar color and the original color is not contained at all,
 * the confidence will still be 100%. Nevertheless, the difference should not be noticeable.
 */
@Service
public class ColorConfidenceCalculatorImpl implements ColorConfidenceCalculator {

    // hardly distinguishable: https://de.wikipedia.org/wiki/Delta_E#Bewertung_von_%CE%94E
    private static final double COLOR_DISTANCE_THRESHOLD = 1.0;

    @Override
    public double calculateConfidence(Color color, Map<Color, Integer> colorHistogram) {
        double similarColors = colorHistogram.getOrDefault(color, 0);
        for (Map.Entry<Color, Integer> colorHistogramEntry : colorHistogram.entrySet()) {
            Color otherColor = colorHistogramEntry.getKey();
            if (color.equals(otherColor)) {
                continue;
            }
            if (ColorDistanceUtil.getColorDistance(color, otherColor) < COLOR_DISTANCE_THRESHOLD) {
                similarColors += colorHistogramEntry.getValue();
            }
        }
        return similarColors / colorHistogram.values().stream().mapToInt(v -> v).sum();
    }

}
