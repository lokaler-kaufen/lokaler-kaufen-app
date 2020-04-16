package de.qaware.mercury.business.image;

import lombok.Value;

@Value(staticConstructor = "of")
public class ColorFindResult {
    String hexColor;
    double confidence;
}
