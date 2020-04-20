package de.qaware.mercury.business.image;

import de.qaware.mercury.business.image.color.ColorFindResult;

import java.io.InputStream;

/**
 * Analyzes a given image and finds the most probable background color.
 */
public interface ImageAnalyzer {

    /**
     * Get the most probable background color of the given image.
     *
     * @param imageId The image id
     * @param data    An input stream of the image file to analyze (e.g. a jpeg or png).
     * @return The identified color and the confidence in the analysis.
     */
    ColorFindResult getBackgroundColor(Image.Id imageId, InputStream data);
}
