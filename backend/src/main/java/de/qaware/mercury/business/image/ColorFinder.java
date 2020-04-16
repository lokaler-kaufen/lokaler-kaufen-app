package de.qaware.mercury.business.image;

import java.io.InputStream;

public interface ColorFinder {
    ColorFindResult getDominantColor(Image.Id imageId, InputStream data);
}
