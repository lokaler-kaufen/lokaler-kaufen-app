package de.qaware.mercury.business.image.color.analysis.impl;

import de.qaware.mercury.business.image.ImageException;
import de.qaware.mercury.business.image.color.Color;
import de.qaware.mercury.business.image.color.analysis.DominantColorFinder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * A very simple implementation of the color histogram analyzer.
 * <p>
 * It finds the most common color in the given histogram.
 */
@Service
public class DominantColorFinderImpl implements DominantColorFinder {

    @Override
    public Color findDominantColor(Map<Color, Integer> colorHistogram) {
        Optional<Map.Entry<Color, Integer>> topColorEntry = colorHistogram.entrySet().stream()
            .max(Map.Entry.comparingByValue());

        if (topColorEntry.isEmpty()) {
            throw new ImageException("Cannot find dominant color");
        }

        return topColorEntry.get().getKey();
    }

}
