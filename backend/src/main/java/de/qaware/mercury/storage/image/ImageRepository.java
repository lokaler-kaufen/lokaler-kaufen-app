package de.qaware.mercury.storage.image;

import de.qaware.mercury.image.Image;

import java.io.InputStream;

public interface ImageRepository {
    void store(Image image, InputStream data);
}
