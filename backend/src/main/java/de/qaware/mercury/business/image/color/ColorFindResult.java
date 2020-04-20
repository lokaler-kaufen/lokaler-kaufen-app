package de.qaware.mercury.business.image.color;

import lombok.Value;

/**
 * The return tuple of the ColorFinder interface.
 */
@Value(staticConstructor = "of")
public class ColorFindResult {

    /**
     * A hex color string, e.g. "#FFAF10'. Includes the '#' sign.
     */
    String hexColor;

    /**
     * The confidence in '%' (a double value from 0.0 to 1.0).
     * Higher means a more confident result. The confidence is determined by
     * how much of the analyzed part of the image has this or a very similar color.
     * <p>
     * Example: 0.3 means that about a third of the relevant image space (e.g. its border)
     * has this or a very similar color.
     */
    double confidence;
}
