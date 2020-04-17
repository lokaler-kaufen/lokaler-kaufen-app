package de.qaware.mercury.business.image;

import de.qaware.mercury.business.shop.Shop;

import java.io.InputStream;
import java.net.URI;

public interface ImageService {
    Image addImage(Shop.Id shopId, InputStream data);

    URI generatePublicUrl(Image.Id imageId);

    InputStream loadImage(Image.Id imageId) throws ImageNotFoundException;

    String getImageColor(Image.Id imageId) throws ImageNotFoundException;

    ImageFormat getImageFormat();

    boolean hasImage(Image.Id imageId);

    void deleteImage(Image.Id imageId);
}
