package de.qaware.mercury.business.image.color.analysis.graphicsmagick;

import java.io.InputStream;

/**
 * Converts an image (e.g. JPEG or PNG) to a GraphicsMagick string representation.
 * <p>
 * The string contains an entry for each pixel with its location and color (r,g,b and hex).
 */
public interface GMImageToTextConverter {

    /**
     * Converts the given image to its GraphicsMagick text representation. The image is
     * resized to a given target size while remaining its aspect ratio.
     *
     * @param image      The input stream to the image file (e.g. a JPEG or PNG image).
     * @param targetSize The target size of the largest side (width or height). The other side will be
     *                   scaled accordingly while maintaining the aspect ratio of the original image.
     * @return A string containing a line for each pixel. The format for each pixel is:
     * "(x,y): (r,g,b) hex", e.g. "1,0: ( 78,175, 52) #4EAF34"
     */
    String convertImageToText(InputStream image, int targetSize);
}
