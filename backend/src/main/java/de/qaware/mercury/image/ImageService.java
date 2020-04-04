package de.qaware.mercury.image;

import java.io.InputStream;
import java.net.URI;

public interface ImageService {
    Image addImage(InputStream image);

    URI generatePublicUrl(Image.Id image);
}
