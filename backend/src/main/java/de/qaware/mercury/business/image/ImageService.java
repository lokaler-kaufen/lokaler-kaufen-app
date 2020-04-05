package de.qaware.mercury.business.image;

import de.qaware.mercury.business.shop.Shop;
import org.springframework.lang.Nullable;

import java.io.InputStream;
import java.net.URI;

public interface ImageService {
    Image addImage(Shop.Id shopId, InputStream data);

    @Nullable
    URI generatePublicUrl(@Nullable Image.Id imageId);

    InputStream loadImage(Image.Id imageId) throws ImageNotFoundException;

    ImageFormat getImageFormat();
}
