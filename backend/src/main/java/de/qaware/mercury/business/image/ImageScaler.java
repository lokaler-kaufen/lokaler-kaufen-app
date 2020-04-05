package de.qaware.mercury.business.image;

import java.io.InputStream;

public interface ImageScaler {
    InputStream scale(Image.Id imageId, InputStream data, int maxSize, ImageFormat format, int quality);
}
