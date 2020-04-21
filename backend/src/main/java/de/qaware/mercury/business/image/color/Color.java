package de.qaware.mercury.business.image.color;

import lombok.Value;

/**
 * A simple container for the colors, containing both, an RGB and a hex representation of the color.
 */
@Value(staticConstructor = "of")
public class Color {
    int red;
    int green;
    int blue;
    String hex;

    /**
     * Private constructor to perform validation for static constructor ('of').
     *
     * @param red   r component
     * @param green g component
     * @param blue  b component
     * @param hex   hex representation, (e.g. '#FFAF10').
     */
    @SuppressWarnings("java:S1144") // false positive, this is used by the static "of" constructor
    private Color(int red, int green, int blue, String hex) {
        if (red < 0 || red > 255 ||
            green < 0 || green > 255 ||
            blue < 0 || blue > 255) {
            throw new IllegalArgumentException("Color values must be between 0-255");
        }
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hex = hex;
    }
}
