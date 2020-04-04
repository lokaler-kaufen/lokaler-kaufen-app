package de.qaware.mercury.image.impl;

import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.image.Image;
import de.qaware.mercury.image.ImageConfigurationProperties;
import de.qaware.mercury.image.ImageService;
import de.qaware.mercury.storage.image.ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
@EnableConfigurationProperties(ImageConfigurationProperties.class)
class ImageServiceImpl implements ImageService {
    private static final String EXTENSION = ".jpeg";

    private final UUIDFactory uuidFactory;
    private final ImageRepository imageRepository;
    private final ImageConfigurationProperties configuration;

    @Override
    public Image addImage(InputStream data) {
        Image image = new Image(Image.Id.random(uuidFactory));

        // TODO: Scale image
        imageRepository.store(image.getId(), generateFilename(image.getId()), data);

        return image;
    }

    private String generateFilename(Image.Id imageId) {
        return imageId.getId() + EXTENSION;
    }

    @Override
    public URI generatePublicUrl(Image.Id imageId) {
        return URI.create(
            configuration.getPublicUrlTemplate().replace("{{ filename }}", generateFilename(imageId))
        );
    }
}
