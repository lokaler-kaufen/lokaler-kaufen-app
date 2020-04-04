package de.qaware.mercury.storage.image;

import de.qaware.mercury.image.Image;
import de.qaware.mercury.image.ImageNotFoundException;

import java.io.InputStream;

public interface ImageRepository {
    void store(Image.Id imageId, String filename, InputStream data);

    InputStream loadImage(Image.Id imageId, String filename) throws ImageNotFoundException;
}
