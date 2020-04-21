package de.qaware.mercury.business.image.impl;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageAnalyzer;
import de.qaware.mercury.business.image.color.Color;
import de.qaware.mercury.business.image.color.ColorFindResult;
import de.qaware.mercury.business.image.color.analysis.ColorConfidenceCalculator;
import de.qaware.mercury.business.image.color.analysis.ColorHistogramCreator;
import de.qaware.mercury.business.image.color.analysis.DominantColorFinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * A simple implementation of the color finder.
 * It works like that:
 * 1. Create a histogram of the colors in a given image
 * 2. Find the most prominent color in the given image
 * 3. Calculate the confidence in the identified color
 */
@Service
@Slf4j
public class ImageAnalyzerImpl implements ImageAnalyzer {
    private static final int RESIZE_RESOLUTION = 40;

    private final ColorHistogramCreator colorHistogramCreator;
    private final DominantColorFinder dominantColorFinder;
    private final ColorConfidenceCalculator colorConfidenceCalculator;

    public ImageAnalyzerImpl(ColorHistogramCreator colorHistogramCreator, DominantColorFinder dominantColorFinder, ColorConfidenceCalculator colorConfidenceCalculator) {
        this.colorHistogramCreator = colorHistogramCreator;
        this.dominantColorFinder = dominantColorFinder;
        this.colorConfidenceCalculator = colorConfidenceCalculator;
    }


    @Override
    public ColorFindResult getBackgroundColor(Image.Id imageId, InputStream data) {
        Map<Color, Integer> colorHistogram = colorHistogramCreator.createImageHistogram(data, RESIZE_RESOLUTION);
        Color dominantColor = dominantColorFinder.findDominantColor(colorHistogram);
        double confidence = colorConfidenceCalculator.calculateConfidence(dominantColor, colorHistogram);
        return ColorFindResult.of(dominantColor.getHex(), confidence);
    }
}
