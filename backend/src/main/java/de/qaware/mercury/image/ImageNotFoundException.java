package de.qaware.mercury.image;

import de.qaware.mercury.business.BusinessException;

public class ImageNotFoundException extends BusinessException {
    public ImageNotFoundException(Image.Id imageId) {
        super(String.format("Image with id '%s' not found", imageId));
    }
}
