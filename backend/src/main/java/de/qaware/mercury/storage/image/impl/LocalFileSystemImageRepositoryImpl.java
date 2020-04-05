package de.qaware.mercury.storage.image.impl;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageConfigurationProperties;
import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.storage.image.ImageRepository;
import de.qaware.mercury.storage.image.ImageStorageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
@EnableConfigurationProperties(ImageConfigurationProperties.class)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class LocalFileSystemImageRepositoryImpl implements ImageRepository {
    private final ImageConfigurationProperties configuration;
    private final AtomicBoolean storageDirectoryCreated = new AtomicBoolean(false);

    @Override
    public void store(Image.Id imageId, String filename, InputStream data) {
        createStorageDirectory();

        Path path = getImagePath(filename);
        log.debug("Storing image {} to '{}' ...", imageId, path);

        try (OutputStream stream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
            long transferred = data.transferTo(stream);
            log.debug("Stored image {} ({} bytes)", imageId, transferred);
        } catch (IOException e) {
            throw new ImageStorageException(String.format("Failed to storage image %s to '%s'", imageId, path), e);
        }
    }

    @Override
    public InputStream loadImage(Image.Id imageId, String filename) throws ImageNotFoundException {
        log.debug("Loading image {}", imageId);

        Path path = getImagePath(filename);
        if (!Files.exists(path)) {
            throw new ImageNotFoundException(imageId);
        }

        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new ImageStorageException(String.format("Failed to load image %s from %s", imageId, path), e);
        }
    }

    @Override
    public boolean hasImage(Image.Id imageId, String filename) {
        Path path = getImagePath(filename);
        return Files.exists(path);
    }

    @Override
    public void deleteImage(Image.Id imageId, String filename) {
        Path path = getImagePath(filename);
        log.debug("Deleting image {} in file {}", imageId, path);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ImageStorageException(String.format("Failed to delete image %s from %s", imageId, path), e);
        }
    }

    private Path getImagePath(String filename) {
        return configuration.getStorageLocationAsPath().resolve(filename).toAbsolutePath();
    }

    private void createStorageDirectory() {
        // Just do that once
        if (storageDirectoryCreated.compareAndSet(false, true)) {
            Path path = configuration.getStorageLocationAsPath().toAbsolutePath();
            log.debug("Creating storage directory '{}'", path);
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new ImageStorageException(String.format("Failed to create directory '%s'", path), e);
            }
        }
    }
}
