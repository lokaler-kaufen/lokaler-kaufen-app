package de.qaware.mercury.business.image.color.analysis;

import de.qaware.mercury.business.image.color.Color;

import java.util.Map;

/**
 * Finds the dominant color in a given color histogram.
 */
public interface DominantColorFinder {

    /**
     * Finds the dominant color in the given color histogram.
     *
     * @param colorHistogram a histogram of the colors in an image
     * @return the dominant color in the histogram.
     */
    Color findDominantColor(Map<Color, Integer> colorHistogram);
}
