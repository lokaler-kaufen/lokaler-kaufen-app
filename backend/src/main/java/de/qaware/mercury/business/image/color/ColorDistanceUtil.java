package de.qaware.mercury.business.image.color;

import lombok.Value;

/**
 * Calculates the subjectively perceived color distance Delta E for RGB colors.
 * <p>
 * See more at https://de.wikipedia.org/wiki/Delta_E
 */
public class ColorDistanceUtil {

    private ColorDistanceUtil() {
        // prevent instances
    }

    // These constants are used for converting the CIE1931 color space to the more subjective LAB Space.
    // These are the D65 values which assume a light temperature of 6500k. This temperature is closer to
    // the LED white produced by screens than the alternative (D50@5000k, more of a warm white).

    // Also the 10 degree values are used, since we cannot assume a perfect 2 degree viewing angle for computer screens.
    private static final double XN10 = 94.811;
    private static final double YN10 = 100;
    private static final double ZN10 = 107.304;

    /**
     * Returns the Delta E distance between two given colors.
     *
     * The calculation is based on the RGB values only, the hex value is ignored.
     *
     * 0.0 - 0.5: practically indistinguishable
     * 0.5 - 1.0: hardly distinguishable
     * 1.0 - 2.0: small color difference
     * 2.0 - 4.0: perceived color difference
     * 4.0 - 5.0: hardly the same base color (e.g. green)
     *     > 5.0: different color
     *
     * See: https://de.wikipedia.org/wiki/Delta_E#Bewertung_von_%CE%94E
     * @param color1 The first color (p)
     * @param color2 The second color (v)
     * @return The Delta E distance.
     */
    public static double getColorDistance(Color color1, Color color2) {
        // Conversion to lab happens via CIE1931
        // https://de.wikipedia.org/wiki/Lab-Farbraum#Umrechnung_von_RGB_zu_Lab
        CIE1931Color cie1931Color1 = rgbToCie1931(color1.getRed(), color1.getGreen(), color1.getBlue());
        CIE1931Color cie1931Color2 = rgbToCie1931(color2.getRed(), color2.getGreen(), color2.getBlue());

        LabColor labColor1 = cie1931ToLab(cie1931Color1);
        LabColor labColor2 = cie1931ToLab(cie1931Color2);

        return getLabDistance(labColor1, labColor2);
    }

    /**
     * Converts an RGB color into the CIE1931 color space (XYZ based)
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return The CIE1931 representation of the given RGB color.
     */
    private static CIE1931Color rgbToCie1931(int r, int g, int b) {
        // https://de.wikipedia.org/wiki/Lab-Farbraum#Umrechnung_von_RGB_zu_Lab
        double x = 0.4123464 * r + 0.3575761 * g + 0.1804375 * b;
        double y = 0.2126729 + 0.7151522 * g + 0.0721750 * b;
        double z = 0.0193339 * r + 0.1191920 * g + 0.9503041 * b;
        return CIE1931Color.of(x, y, z);
    }

    /**
     * Converts the CIE1931 color into the Lab color space
     * @param color The CIE1931 color
     * @return The lab color, based on N10 constants and D65 light.
     */
    private static LabColor cie1931ToLab(CIE1931Color color) {
        // https://de.wikipedia.org/wiki/Lab-Farbraum#Umrechnung_von_XYZ_zu_Lab
        double l = 116 * Math.cbrt(color.y / YN10) - 16;
        double a = 500 * (Math.cbrt(color.x / XN10) - Math.cbrt(color.y / YN10));
        double b = 200 * (Math.cbrt(color.y / YN10) - Math.cbrt(color.z / ZN10));
        return LabColor.of(l, a, b);
    }

    /**
     * Calculates the Delta E color distance between two Lab colors.
     * @param color1 The first color (p)
     * @param color2 The second color (v)
     * @return The Delta E color distance.
     */
    private static double getLabDistance(LabColor color1, LabColor color2) {
        // https://de.wikipedia.org/wiki/Delta_E#Farbabstand_Delta_E
        return Math.sqrt(
            Math.pow(color1.l - color2.l, 2) +
                Math.pow(color1.a - color2.a, 2) +
                Math.pow(color1.b - color2.b, 2)
        );
    }

    @Value(staticConstructor = "of")
    private static class CIE1931Color {
        double x;
        double y;
        double z;
    }

    @Value(staticConstructor = "of")
    private static class LabColor {
        double l;
        double a;
        double b;
    }
}
