package de.qaware.mercury.business.image.impl;

import de.qaware.mercury.business.image.Image;
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

    @Override
    public Image addImage(Shop.Id shopId, InputStream data) {
        log.info("Adding image for shop {}", shopId);

        // TODO: Check maximum images for shop

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
    public ImageFormat getImageFormat() {
        return IMAGE_FORMAT;
    }

    private String generateFilename(Image.Id imageId) {
        return imageId.getId() + IMAGE_FORMAT.getExtension();
    }

    @Override
    @Nullable
    public URI generatePublicUrl(@Nullable Image.Id imageId) {
        if (imageId == null) {
            return null;
        }
        return URI.create(
            configuration.getPublicUrlTemplate().replace("{{ filename }}", generateFilename(imageId))
        );
    }
}
