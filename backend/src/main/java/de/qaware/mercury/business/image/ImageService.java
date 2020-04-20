package de.qaware.mercury.business.image;

import de.qaware.mercury.business.shop.Shop;

import java.io.InputStream;
import java.net.URI;

public interface ImageService {
    Image addImage(Shop.Id shopId, InputStream data);

    URI generatePublicUrl(Image.Id imageId);

    InputStream loadImage(Image.Id imageId) throws ImageNotFoundException;

    /**
     * Get the probable background color of the image with the given ID.
     *
     * @param imageId the internal ID of the image as referenced in the shop.
     * @return A hex color string (e.g. '#FFAF10', including the '#' sign).
     * @throws ImageNotFoundException If the image with the given ID cannot be found.
     */
    String getImageBackgroundColor(Image.Id imageId) throws ImageNotFoundException;

    ImageFormat getImageFormat();

    boolean hasImage(Image.Id imageId);

    void deleteImage(Image.Id imageId);
}
