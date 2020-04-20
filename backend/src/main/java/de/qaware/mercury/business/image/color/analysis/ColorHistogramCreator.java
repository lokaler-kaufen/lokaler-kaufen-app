package de.qaware.mercury.business.image.color.analysis;

import de.qaware.mercury.business.image.color.Color;

import java.io.InputStream;
import java.util.Map;

/**
 * Creates a histogram of the colors in an image.
 */
public interface ColorHistogramCreator {

    /**
     * Analyzes the image in the given inputStream and returns a color histogram.
     * <p>
     * Deflates the image before the analysis, in order to reduce noise. The dimensions of the deflated image
     * are determined by the analysisSize which will be the size of the longest side (width or height). The smaller
     * side will be scaled according to the original aspect ration of the given image.
     * <p>
     * The histogram can be selective of the contents of the image, depending on the implementation (e.g. just the border,
     * or the whole image).
     *
     * @param inputStream  the image to analyze
     * @param analysisSize the length of the longest side to which the image will be scaled before the analysis
     * @return a histogram mapping each color to its absolute frequency in the deflated image
     */
    Map<Color, Integer> createImageHistogram(InputStream inputStream, int analysisSize);
}
