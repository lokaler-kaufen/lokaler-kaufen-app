package de.qaware.mercury.business.image;

import lombok.Value;

public class ColorDistanceUtil {
    private static final double XN10 = 94.811;
    private static final double YN10 = 100;
    private static final double ZN10 = 107.304;

    public static double getColorDistance(Color color1, Color color2) {
        CIE1931Color cie1931Color1 = rgbToCie1931(color1.getRed(), color1.getGreen(), color1.getBlue());
        CIE1931Color cie1931Color2 = rgbToCie1931(color2.getRed(), color2.getGreen(), color2.getBlue());
        LabColor labColor1 = cie1931ToLab(cie1931Color1);
        LabColor labColor2 = cie1931ToLab(cie1931Color2);
        return getLabDistance(labColor1, labColor2);
    }

    private static CIE1931Color rgbToCie1931(int r, int g, int b) {
        // https://de.wikipedia.org/wiki/Lab-Farbraum#Umrechnung_von_RGB_zu_Lab
        double x = 0.4123464 * r + 0.3575761 * g + 0.1804375 * b;
        double y = 0.2126729 + 0.7151522 * g + 0.0721750 * b;
        double z = 0.0193339 * r + 0.1191920 * g + 0.9503041 * b;
        return CIE1931Color.of(x, y, z);
    }

    private static LabColor cie1931ToLab(CIE1931Color color) {
        byte L = (byte) Math.round(116 * Math.cbrt(color.y / YN10) - 16);
        byte a = (byte) Math.round(500 * (Math.cbrt(color.x / XN10) - Math.cbrt(color.y / YN10)));
        byte b = (byte) Math.round(200 * (Math.cbrt(color.y / YN10) - Math.cbrt(color.z / ZN10)));
        return LabColor.of(L, a, b);
    }

    private static double getLabDistance(LabColor color1, LabColor color2) {
        return Math.sqrt(
            Math.pow( color1.l - color2.l, 2) +
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
        byte l;
        byte a;
        byte b;
    }
}
