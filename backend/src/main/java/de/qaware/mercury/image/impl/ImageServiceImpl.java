package de.qaware.mercury.image.impl;

import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.image.Image;
import de.qaware.mercury.image.ImageService;
import de.qaware.mercury.storage.image.ImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class ImageServiceImpl implements ImageService {
    private final UUIDFactory uuidFactory;
    private final ImageRepository imageRepository;

    @Override
    public Image addImage(InputStream data) {
        Image image = new Image(Image.Id.random(uuidFactory));

        // TODO: Scale image
        imageRepository.store(image, data);

        return image;
    }

    @Override
    public URI generatePublicUrl(Image.Id image) {
        // TODO: Generate URI

        return URI.create("http://localhost/foo.jpg");
    }
}
