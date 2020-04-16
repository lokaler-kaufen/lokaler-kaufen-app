package de.qaware.mercury.business.image;

import lombok.Value;

@Value(staticConstructor = "of")
public class Color {
    int red;
    int green;
    int blue;
    String hex;

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
