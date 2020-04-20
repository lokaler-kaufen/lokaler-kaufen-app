package de.qaware.mercury.business.image.impl;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageAnalyzer;
import de.qaware.mercury.business.image.ImageConfigurationProperties;
import de.qaware.mercury.business.image.ImageException;
import de.qaware.mercury.business.image.ImageFormat;
import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.business.image.ImageScaler;
import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.image.ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
@EnableConfigurationProperties(ImageConfigurationProperties.class)
class ImageServiceImpl implements ImageService {
    private static final ImageFormat IMAGE_FORMAT = ImageFormat.JPEG;

    private final UUIDFactory uuidFactory;
    private final ImageRepository imageRepository;
    private final ImageScaler imageScaler;
    private final ImageConfigurationProperties configuration;
    private final ImageAnalyzer imageAnalyzer;

    @Override
    public Image addImage(Shop.Id shopId, InputStream data) {
        log.info("Adding image for shop {}", shopId);

        Image image = new Image(Image.Id.random(uuidFactory));

        try (InputStream stream = imageScaler.scale(image.getId(), data, configuration.getSize(), IMAGE_FORMAT, configuration.getQuality())) {
            imageRepository.store(image.getId(), generateFilename(image.getId()), stream);
        } catch (IOException e) {
            throw new ImageException(String.format("Failed to add image for shop '%s'", shopId), e);
        }

        return image;
    }

    @Override
    public InputStream loadImage(Image.Id imageId) throws ImageNotFoundException {
        return imageRepository.loadImage(imageId, generateFilename(imageId));
    }

    @Override
    public String getImageBackgroundColor(Image.Id imageId) throws ImageNotFoundException {

        try (InputStream image = loadImage(imageId)) {
            // At the moment, we're returning the color regardless of the confidence.
            // There have not been any false positives yet, so it's hard to pinpoint the acceptable
            // threshold correctly. If there are any false positives, this is the right place to return
            // a default value (e.g. #FFFFFF) if the confidence value is to low.
            return imageAnalyzer.getBackgroundColor(imageId, image).getHexColor();
        } catch (IOException e) {
            throw new ImageException("Failed to load image", e);
        }
    }

    @Override
    public ImageFormat getImageFormat() {
        return IMAGE_FORMAT;
    }

    @Override
    @Nullable
    public URI generatePublicUrl(Image.Id imageId) {
        return URI.create(
            configuration.getPublicUrlTemplate().replace("{{ filename }}", generateFilename(imageId))
        );
    }

    @Override
    public boolean hasImage(Image.Id imageId) {
        return imageRepository.hasImage(imageId, generateFilename(imageId));
    }

    @Override
    public void deleteImage(Image.Id imageId) {
        log.info("Deleting image {}", imageId);

        imageRepository.deleteImage(imageId, generateFilename(imageId));
    }

    private String generateFilename(Image.Id imageId) {
        return imageId.getId() + IMAGE_FORMAT.getExtension();
    }
}
